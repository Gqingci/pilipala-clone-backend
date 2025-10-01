package com.pilipala.service.impl;

import com.pilipala.component.RedisComponent;
import com.pilipala.dto.LoginFormDTO;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.UserGenderEnum;
import com.pilipala.entity.enums.UserStatusEnum;
import com.pilipala.entity.query.UsersQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.Users;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.mappers.UsersMapper;
import com.pilipala.utils.Constants;
import com.pilipala.utils.CopyUtils;
import com.pilipala.utils.RedisConstants;
import com.pilipala.utils.StringUtils;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.UsersService;

/**
 * @author Gqingci
 * @Description: 对应的ServiceImpl
 * @date: 2025/09/15
 */

@Service("usersService")
public class UsersServiceImpl implements UsersService {

    @Resource
    private UsersMapper<Users, UsersQuery> usersMapper;

    @Resource
    private RedisComponent redisComponent;
    /**
     * 根据条件查询列表
     */
    @Override
    public List<Users> findListByParam(UsersQuery query) {
        return this.usersMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    @Override
    public Integer findCountByParam(UsersQuery query) {
        return this.usersMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVO<Users> findListByPage(UsersQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<Users> list = this.findListByParam(query);
        PaginationResultVO<Users> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Users bean) {
        return this.usersMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Users> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.usersMapper.insertBatch(listBean);
    }

    /**
     * 新增或者修改
     */
    @Override
    public Integer addOrUpdate(Users users) {
        return this.usersMapper.insertOrUpdate(users);
    }

    /**
     * 批量新增或修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Users> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.usersMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据Id查询
     */
    @Override
    public Users getById(String id) {
        return this.usersMapper.selectById(id);
    }

    /**
     * 根据Id更新
     */
    @Override
    public Integer updateById(Users bean, String id) {
        return this.usersMapper.updateById(bean, id);
    }

    /**
     * 根据Id删除
     */
    @Override
    public Integer deleteById(String id) {
        return this.usersMapper.deleteById(id);
    }

    /**
     * 根据Username查询
     */
    @Override
    public Users getByUsername(String username) {
        return this.usersMapper.selectByUsername(username);
    }

    /**
     * 根据Username更新
     */
    @Override
    public Integer updateByUsername(Users bean, String username) {
        return this.usersMapper.updateByUsername(bean, username);
    }

    /**
     * 根据Username删除
     */
    @Override
    public Integer deleteByUsername(String username) {
        return this.usersMapper.deleteByUsername(username);
    }

    /**
     * 根据Email查询
     */
    @Override
    public Users getByEmail(String email) {
        return this.usersMapper.selectByEmail(email);
    }

    /**
     * 根据Email更新
     */
    @Override
    public Integer updateByEmail(Users bean, String email) {
        return this.usersMapper.updateByEmail(bean, email);
    }

    /**
     * 根据Email删除
     */
    @Override
    public Integer deleteByEmail(String email) {
        return this.usersMapper.deleteByEmail(email);
    }

    @Override
    public void register(String email, String registerPassword) {
        // 1. 检查邮箱是否已存在
        Users existingUser = this.usersMapper.selectByEmail(email);
        if (existingUser != null) {
            throw new BusinessException("邮箱已被注册。");
        }

        // 3. 构建用户对象
        Users user = new Users();
        String userId = StringUtils.getRandomNumberString(Constants.USERID_MAX_LENGTH);
        user.setId(userId);
        // 生成默认用户名
        user.setUsername(generateDefaultUsername());
        // 密码加密
        user.setPasswordHash(StringUtils.encodeByHash(registerPassword));
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setIsActive(UserStatusEnum.ENABLE.getStatus());
        user.setGender(UserGenderEnum.SECRECY.getType());

        this.usersMapper.insert(user);
    }

    @Override
    public UserTokenInfoDTO login(LoginFormDTO loginForm, String ip) {
        Users existingUser = this.usersMapper.selectByEmail(loginForm.getEmail());
        if(existingUser == null || !StringUtils.matches(loginForm.getPassword(), existingUser.getPasswordHash())) {
            throw new BusinessException("邮箱或密码错误。");
        }

        if(UserStatusEnum.DISABLE.getStatus().equals(existingUser.getIsActive())) {
            throw new BusinessException("账号已封禁。");
        }

        Users updateUser = new Users();
        updateUser.setLastLogin(new Date());
        updateUser.setLastLoginIp(ip);
        this.usersMapper.updateById(updateUser, existingUser.getId());

        UserTokenInfoDTO userTokenInfoDTO = CopyUtils.copy(existingUser, UserTokenInfoDTO.class);
        redisComponent.saveToken(userTokenInfoDTO);

        return userTokenInfoDTO;
    }

    private String generateDefaultUsername() {
        String prefix = "p站用户";
        int number = 10000 + new Random().nextInt(90000);

        String username = prefix + number;

        // 确保数据库唯一
        while (this.usersMapper.selectByUsername(username) != null) {
            number = 10000 + new Random().nextInt(90000);
            username = prefix + number;
        }

        return username;
    }
}