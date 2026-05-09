package com.pilipala.component;

import com.pilipala.config.AppConfig;
import com.pilipala.dto.VideoInfoEsDTO;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.entity.enums.SearchOrderTypeEnum;
import com.pilipala.entity.po.Users;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.query.UsersQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.mappers.UsersMapper;
import com.pilipala.utils.CopyUtils;
import com.pilipala.utils.JsonUtils;
import com.pilipala.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("esSearchComponent")
@Slf4j
public class EsSearchComponent {
    @Resource
    private UsersMapper<Users, UsersQuery> usersMapper;

    @Resource
    private AppConfig appConfig;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    public void createIndex() {
        try {
            if (isExistIndex()) {
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(appConfig.getEsIndexVideoName());
            request.settings("{\"analysis\": {\n" + "\"analyzer\": {\n" + "\"comma\": {\n" + "\"type\": \"pattern\",\n" + "\"pattern\": \",\"\n" + "}\n" + "}\n" + "}}", XContentType.JSON);
            request.mapping("{\"properties\": {\n" +
                    "    \"videoId\":{\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"index\": false\n" +
                    "    },\n" +
                    "    \"userId\":{\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"index\": false\n" +
                    "    },\n" +
                    "    \"videoCover\":{\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"index\": false\n" +
                    "    },\n" +
                    "    \"videoName\":{\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"analyzer\": \"ik_max_word\"\n" +
                    "    },\n" +
                    "    \"tags\":{\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"analyzer\": \"comma\"\n" +
                    "    },\n" +
                    "    \"playCount\":{\n" +
                    "      \"type\":\"integer\",\n" +
                    "      \"index\":false\n" +
                    "    },\n" +
                    "    \"danmuCount\":{\n" +
                    "      \"type\":\"integer\",\n" +
                    "      \"index\":false\n" +
                    "    },\n" +
                    "    \"collectCount\":{\n" +
                    "      \"type\":\"integer\",\n" +
                    "      \"index\":false\n" +
                    "    },\n" +
                    "    \"createTime\":{\n" +
                    "      \"type\":\"date\",\n" +
                    "      \"format\": \"yyyy-MM-dd HH:mm:ss\",\n" +
                    "      \"index\": false\n" +
                    "    }\n" +
                    "  }}", XContentType.JSON);
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            Boolean acknowledged = response.isAcknowledged();
            if (!acknowledged) {
                throw new BusinessException("es创建索引失败");
            }
        } catch (IOException e) {
            log.error("es创建索引失败");
            throw new BusinessException("es创建索引失败");
        }
    }

    private Boolean isExistIndex() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(appConfig.getEsIndexVideoName());
        return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    public void saveDoc(Video video) {
        try {
            if (docExist(video.getVideoId())) {
                updateDoc(video);
                return;
            }

            VideoInfoEsDTO videoInfoEsDTO = CopyUtils.copy(video, VideoInfoEsDTO.class);
            videoInfoEsDTO.setCollectCount(0);
            videoInfoEsDTO.setPlayCount(0);
            videoInfoEsDTO.setDanmuCount(0);
            IndexRequest request = new IndexRequest(appConfig.getEsIndexVideoName());
            request.id(video.getVideoId()).source(JsonUtils.convertObj2Json(videoInfoEsDTO), XContentType.JSON);
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es保存文档失败");
            throw new BusinessException("es保存文档失败");
        }
    }

    private Boolean docExist(String docId) throws IOException {
        GetRequest request = new GetRequest(appConfig.getEsIndexVideoName(), docId);
        return restHighLevelClient.get(request, RequestOptions.DEFAULT).isExists();
    }

    private void updateDoc(Video video) {
        try {
            video.setLastUpdateTime(null);
            video.setCreateTime(null);

            Map<String, Object> dataMap = new HashMap<>();
            Field[] fields = video.getClass().getDeclaredFields();
            for (Field field : fields) {
                String methodName = "get" + StringUtils.upperCaseFirstLetter(field.getName());
                Method method = video.getClass().getMethod(methodName);
                Object object = method.invoke(video);
                if (object != null) {
                    dataMap.put(field.getName(), object);
                }
            }
            if (dataMap.isEmpty()) {
                return;
            }
            UpdateRequest request = new UpdateRequest(appConfig.getEsIndexVideoName(), video.getVideoId());
            request.doc(dataMap);
            restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es更新文档失败", e);
            throw new BusinessException("保存视频失败");
        }
    }

    public void updateDocCount(String videoId, String fieldName, Integer count) {
        try {
            if (!docExist(videoId)) {
                log.warn("ES 文档不存在 videoId: {}", videoId);
                return;
            }
            UpdateRequest request = new UpdateRequest(appConfig.getEsIndexVideoName(), videoId);
            Script script = new Script(ScriptType.INLINE, "painless", "ctx._source." + fieldName + " += params.count", Collections.singletonMap("count", count));
            restHighLevelClient.update(request.script(script), RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es更新数量失败", e);
            throw new BusinessException("保存到es失败");
        }
    }

    public void delDoc(String videoId) {
        try {
            DeleteRequest request = new DeleteRequest(appConfig.getEsIndexVideoName(), videoId);
            restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es删除文档失败", e);
            throw new BusinessException("删除文档失败");
        }
    }

    public PaginationResultVO<Video> search(String keyword, Integer pageNo, Integer pageSize, Integer orderType, Boolean highlight) {
        try {
            SearchOrderTypeEnum searchOrderTypeEnum = SearchOrderTypeEnum.getByType(orderType);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, "videoName", "tags"));

            // 高亮
            if (highlight) {
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                highlightBuilder.field("videoName");
                highlightBuilder.preTags("<span class='highlight'>");
                highlightBuilder.postTags("</span>");
                searchSourceBuilder.highlighter(highlightBuilder);
            }

            // 排序
            searchSourceBuilder.sort("_score", SortOrder.DESC);
            if (searchOrderTypeEnum != null) {
                searchSourceBuilder.sort(searchOrderTypeEnum.getField(), SortOrder.DESC);
            }

            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? PageSize.SIZE20.getSize() : pageSize;
            searchSourceBuilder.size(pageSize);
            searchSourceBuilder.from((pageNo - 1) * pageSize);

            SearchRequest request = new SearchRequest(appConfig.getEsIndexVideoName());
            request.source(searchSourceBuilder);

            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

            SearchHits searchHits = response.getHits();
            Integer totalCount = (int) searchHits.getTotalHits().value;

            List<Video> videoList = new ArrayList<>();
            List<String> userIdList = new ArrayList<>();

            for (SearchHit hit : searchHits.getHits()) {
                Video video = JsonUtils.convertJson2Obj(hit.getSourceAsString(), Video.class);
                if (hit.getHighlightFields().get("videoName") != null) {
                    video.setVideoName(hit.getHighlightFields().get("videoName").fragments()[0].toString());
                }
                videoList.add(video);
                userIdList.add(video.getUserId());
            }

            UsersQuery usersQuery = new UsersQuery();
            usersQuery.setUserIdList(userIdList);
            List<Users> userList = usersMapper.selectList(usersQuery);
            Map<String, Users> userMap = userList.stream().collect(Collectors.toMap(Users::getId, Function.identity(), (data1, data2) -> data2));
            videoList.forEach(item -> {
                Users user = userMap.get(item.getUserId());
                item.setUsername(user == null ? "" : user.getUsername());
            });

            SimplePage page = new SimplePage(pageNo, totalCount, pageSize);
            PaginationResultVO<Video> resultVO = new PaginationResultVO<>(totalCount, page.getPageSize(), page.getCountTotal(), videoList);
            return resultVO;
        } catch (Exception e) {
            log.error("es搜索失败", e);
            throw new BusinessException("查询失败"+ e.getMessage());
        }
    }
}
