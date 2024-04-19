package cn.itcast.hotel;

import cn.itcast.hotel.service.IHotelService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
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
public class HotelIndexTest {

    private RestHighLevelClient client;
    @Test
    void test(){
        System.out.println(client);
    }
    @Test
    void createHotelIndex() throws IOException {
        //1.创建Request对象
        CreateIndexRequest request=new CreateIndexRequest("hotel");
        //2.准备请求的DSL雨具
        request.source( MappingTemplate, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }
    @Test
    void DeleteHotelIndex() throws IOException {
        //1.创建Request对象
        DeleteIndexRequest request=new DeleteIndexRequest("hotel");
        client.indices().delete(request, RequestOptions.DEFAULT);
    }
    @Test
    void ExistHotelIndex() throws IOException {
        GetIndexRequest getIndexRequest =new GetIndexRequest("hotel"); ;
        Boolean flag=client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(flag);
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
}
