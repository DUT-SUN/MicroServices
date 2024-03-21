package cn.itcast.hotel;

import cn.itcast.hotel.pojo.HotelDoc;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import javax.annotation.processing.Completion;
import java.io.IOException;
import java.util.*;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/19  21:16
 */

public class HotelSearchTest {
    private RestHighLevelClient client;
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
    void SearchAll() throws IOException {

        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.matchAllQuery());
        SearchResponse response=client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        TotalHits totalHits=searchHits.getTotalHits();
        SearchHit[] searchHits1=searchHits.getHits();
        for (SearchHit searchHit:
             searchHits1) {
            String source=searchHit.getSourceAsString();
            HotelDoc hotelDoc= JSON.parseObject(source,HotelDoc.class);
            System.out.println(hotelDoc);
        }
        System.out.println("总条数:"+totalHits.value);
    }
    @Test
    void SearchMatch() throws IOException {

        SearchRequest searchRequest = new SearchRequest("hotel");
        //Object text, String   ... fieldNames
        searchRequest.source().query(QueryBuilders.multiMatchQuery("外滩","name","business"));
        SearchResponse response=client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        TotalHits totalHits=searchHits.getTotalHits();
        SearchHit[] searchHits1=searchHits.getHits();
        for (SearchHit searchHit:
                searchHits1) {
            String source=searchHit.getSourceAsString();
            HotelDoc hotelDoc= JSON.parseObject(source,HotelDoc.class);
            System.out.println(hotelDoc);
        }
        System.out.println("总条数:"+totalHits.value);
    }
    @Test
    void SearchTermRange() throws IOException {

        SearchRequest searchRequest = new SearchRequest("hotel");
        //Object text, String   ... fieldNames
//        searchRequest.source().query(QueryBuilders.termQuery("price",328));
        searchRequest.source().query(QueryBuilders.rangeQuery("price").gte(1000).lte(3000));
        SearchResponse response=client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        TotalHits totalHits=searchHits.getTotalHits();
        SearchHit[] searchHits1=searchHits.getHits();
        for (SearchHit searchHit:
                searchHits1) {
            String source=searchHit.getSourceAsString();
            HotelDoc hotelDoc= JSON.parseObject(source,HotelDoc.class);
            System.out.println(hotelDoc);
        }
        System.out.println("总条数:"+totalHits.value);
    }
    @Test
    void CombineboolSearch() throws IOException {

        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.boolQuery().must(
                QueryBuilders.termQuery("name","如家")
        ).filter(
                QueryBuilders.rangeQuery("price").gte(100).lte(1000)
        ));

        SearchResponse response=client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        TotalHits totalHits=searchHits.getTotalHits();
        SearchHit[] searchHits1=searchHits.getHits();
        for (SearchHit searchHit:
                searchHits1) {
            String source=searchHit.getSourceAsString();
            HotelDoc hotelDoc= JSON.parseObject(source,HotelDoc.class);
            System.out.println(hotelDoc);
        }
        System.out.println("总条数:"+totalHits.value);
    }
@Test
    void SearchLimitPage() throws IOException {

        SearchRequest searchRequest = new SearchRequest("hotel");
    //(String name, SortOrder order)
        searchRequest.source().query(QueryBuilders.matchAllQuery()).from(10).size(10).sort("price", SortOrder.ASC);
        SearchResponse response=client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        TotalHits totalHits=searchHits.getTotalHits();
        SearchHit[] searchHits1=searchHits.getHits();
        for (SearchHit searchHit:
                searchHits1) {
            String source=searchHit.getSourceAsString();
            HotelDoc hotelDoc= JSON.parseObject(source,HotelDoc.class);
            System.out.println(hotelDoc);
        }
        System.out.println("总条数:"+totalHits.value);
    }
    @Test
    void SearchHighLight() throws IOException {

        SearchRequest searchRequest = new SearchRequest("hotel");
//        return this.highlightBuilder;
        searchRequest.source().query(QueryBuilders.matchQuery("all","外滩")).highlighter(new HighlightBuilder().requireFieldMatch(false).field("name").preTags("<em>").postTags("</em>"));
        SearchResponse response=client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        TotalHits totalHits=searchHits.getTotalHits();
        SearchHit[] searchHits1=searchHits.getHits();
        for (SearchHit searchHit:
                searchHits1) {
            String source=searchHit.getSourceAsString();
            HotelDoc hotelDoc= JSON.parseObject(source,HotelDoc.class);
             Map<String, HighlightField > highpart=searchHit.getHighlightFields();
             if(!CollectionUtils.isEmpty(highpart)){
                HighlightField highlightField=highpart.get("name");
                 if(highlightField!=null){
                     String name =highlightField.getFragments()[0].string();
                     hotelDoc.setName(name);
                 }
             }
            System.out.println(hotelDoc);

//            Iterator<String> iterator = source.keySet().iterator();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                String value = String.valueOf(source.get(key));
//                System.out.println("key = " + key + ", value = " + value);
//            }
//            Iterator<Map.Entry<String, HighlightField>> entries = source.entrySet().iterator();
//            while (entries.hasNext()) {
//                Map.Entry<String, HighlightField> entry = entries.next();
//                System.out.println(entry);
//            }


        }
        System.out.println("总条数:"+totalHits.value);
    }

    //桶聚合
    @Test
    void SearchBucketAgg() throws IOException {
        SearchRequest request=new SearchRequest("hotel");
        //2.准备DSL
        request.source().size(0).aggregation(AggregationBuilders
                .terms("brandAgg")
                .size(20)
                .field("brand")
        );
        SearchResponse response=client.search(request,RequestOptions.DEFAULT);
        //获取聚合信息
        Aggregations aggregations= response.getAggregations();
        //获取具体聚合结果
        Terms brandTerms=aggregations.get("brandAgg");
        //获取具体聚合结果中的桶列表
        List<? extends Terms.Bucket> buckets=brandTerms.getBuckets();
        for(Terms.Bucket bucket:buckets){
            //获取每个桶的键转换成String
            String brandName=bucket.getKeyAsString();
            System.out.println(brandName);
        }
    }
    //
    @Test
    void Suggestion() throws IOException {

        SearchRequest request = new SearchRequest("hotel");
        request.source().suggest(new SuggestBuilder().addSuggestion(
                       "suggestions",//定义这是补全的名称
                SuggestBuilders
                        .completionSuggestion("suggestion")
                        .prefix("h")
                        .skipDuplicates(true)
                        .size(10)
                        )
        );
        SearchResponse response=client.search(request,RequestOptions.DEFAULT);
        Suggest suggestions= response.getSuggest();
        CompletionSuggestion suggestion= suggestions.getSuggestion("suggestions");
        for(CompletionSuggestion.Entry.Option option:suggestion.getOptions()){
            String text=option.getText().toString();
            System.out.println(text);
        }
    }
}
