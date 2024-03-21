package cn.itcast.hotel;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static cn.itcast.hotel.constants.HotelConstant.MappingTemplate;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/19  16:13
 */
@SpringBootTest
public class HotelDocumentTest {
    @Autowired
    private IHotelService iHotelService;
    private RestHighLevelClient client;
    @Test
    void test(){
        System.out.println(client);
    }
    @BeforeEach
    void setUp(){
        this.client=new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://81.70.175.60:9200")
        ));
    }
    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }
    @Test
    void TestAddDocument() throws IOException {
       Hotel hotel= iHotelService.getById(607915L);
        HotelDoc hotelDoc=new HotelDoc(hotel);
        IndexRequest request= new IndexRequest("hotel").id(hotelDoc.getId().toString());
       request.source(JSON.toJSONString(hotelDoc),XContentType.JSON);
        client.index(request,RequestOptions.DEFAULT);
    }
    @Test
    void TestGetDocument() throws IOException {
        GetRequest request= new GetRequest("hotel").id("607915");
       GetResponse response= client.get(request,RequestOptions.DEFAULT);
       String json=response.getSourceAsString();
        System.out.println(json);
    }
    @Test
    void TestupdateDocument() throws IOException {

        UpdateRequest updateRequest = new UpdateRequest("hotel","60223");
        updateRequest.doc(
             "isAD", "true"
        );
        client.update(updateRequest,RequestOptions.DEFAULT);
    }
    @Test
    void TestDeleteDocument() throws IOException {

        DeleteRequest deleteRequest = new DeleteRequest("hotel","60223");
        client.delete(deleteRequest,RequestOptions.DEFAULT);
    }
    @Test
    void testBulkRequest() throws IOException {
        //批量数据库查询数据
        List<Hotel>hotels=iHotelService.list();
        //1.创建request
        System.out.println(hotels.size());
        BulkRequest request=new BulkRequest();
        //转换为文档类型HotelDoc
        for(Hotel hotel:hotels){
            HotelDoc hotelDoc=new HotelDoc(hotel);
            request.add(new IndexRequest("hotel").id(hotelDoc.getId().toString()).source(JSON.toJSONString(hotelDoc),XContentType.JSON));
        }
        //2.设置request属性
        client.bulk(request,RequestOptions.DEFAULT);
    }
}
