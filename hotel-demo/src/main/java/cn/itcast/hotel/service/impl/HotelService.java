package cn.itcast.hotel.service.impl;

import cn.itcast.hotel.mapper.HotelMapper;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.RequestParams;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {
   @Autowired
   private RestHighLevelClient client;
    @Override
    public PageResult search(RequestParams params) throws IOException {
        try{
        //准备request
        SearchRequest searchRequest = new SearchRequest("hotel");
            FunctionScoreQueryBuilder  boolQueryBuilder=buildQuery( params,searchRequest);
        searchRequest.source().query(boolQueryBuilder);//过滤后的数据
            //按照地理位置进行排序
            String address=params.getLocation();
            if(address==null || "".equals(address)){
            }else{
                searchRequest.source().sort(SortBuilders
                        .geoDistanceSort("location",new GeoPoint(address))
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS)
                );
            }
            //分页
        int page=params.getPage();
        int size=params.getSize();
        searchRequest.source().from((page-1)*size).size(size);

        //发送请求，得到响应
        SearchResponse response=client.search(searchRequest, RequestOptions.DEFAULT);
            return handleResponse(response);
        }catch (RuntimeException e){
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, List<String>> filters(RequestParams params)  {
        try {
            SearchRequest request=new SearchRequest("hotel");
           FunctionScoreQueryBuilder boolQueryBuilder=buildQuery( params,request);
            request.source().query(boolQueryBuilder);
        //DSL city，brand，starName
        request.source().size(0).aggregation(AggregationBuilders
                .terms("brandAgg")
                .size(20)
                .field("brand")
        );
        request.source().aggregation(AggregationBuilders
                .terms("cityAgg")
                .size(20)
                .field("city")
        );
        request.source().aggregation(AggregationBuilders
                .terms("starNameAgg")
                .size(20)
                .field("starName")
        );
        Map<String, List<String>> map=new HashMap<>();
        SearchResponse response=client.search(request,RequestOptions.DEFAULT);
        Aggregations aggregations=response.getAggregations();
        Terms terms1=aggregations.get("brandAgg");
        List<? extends Terms.Bucket>buckets1=terms1.getBuckets();
        List<String>list1=new ArrayList<>();
        for(Terms.Bucket bucket:buckets1){
            String key=bucket.getKeyAsString();
            list1.add(key);
        }
        Terms terms2=aggregations.get("cityAgg");
        List<? extends Terms.Bucket>buckets2=terms2.getBuckets();
        List<String>list2=new ArrayList<>();
        for(Terms.Bucket bucket:buckets2){
            String key=bucket.getKeyAsString();
            list2.add(key);
        }
        Terms terms=aggregations.get("starNameAgg");
        List<? extends Terms.Bucket>buckets3=terms.getBuckets();
        List<String>list3=new ArrayList<>();
        for(Terms.Bucket bucket:buckets3){
            String key=bucket.getKeyAsString();
            list3.add(key);
        }
        map.put("brand",list1);
        map.put("city",list2);
        map.put("starName",list3);
        return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<String> getSuggestions(String prefix)  {
        try {
            //函数为了补全，返回关键字数组
            SearchRequest request = new SearchRequest("hotel");
            request.source().suggest(new SuggestBuilder().addSuggestion(
                    "suggestions",
                    SuggestBuilders.completionSuggestion("suggestion")
                            .prefix(prefix)
                            .size(20)
                            .skipDuplicates(true)
            ));
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            Suggest suggestions = response.getSuggest();
            CompletionSuggestion suggestion = suggestions.getSuggestion("suggestions");
            List<String> list = new ArrayList<>();
            for (CompletionSuggestion.Entry.Option option : suggestion.getOptions()) {
                list.add(option.getText().toString());
            }
            return list;
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteById(Long id)  {
        try{
        //准备request
        //发送请求
        DeleteRequest deleteRequest = new DeleteRequest("hotel",id.toString());
        client.delete(deleteRequest,RequestOptions.DEFAULT);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertById(Long id) {
        try{
        Hotel hotel=getById(id);
        //获取数据库数据
        HotelDoc hotelDoc=new HotelDoc(hotel);
//        IndexRequest request =new IndexRequest("hotel").id(hotel.getId().toString()) ;
//        request.source((JSON.toJSONString(hotelDoc)), XContentType.JSON);
//        client.index(request,RequestOptions.DEFAULT);
            UpdateRequest request = new UpdateRequest("hotel", hotelDoc.getId().toString());
            // 2. 准备参数
            request.doc(
                    (JSON.toJSONString(hotelDoc)), XContentType.JSON
            );
            // 3. 发起请求
            client.update(request, RequestOptions.DEFAULT);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private FunctionScoreQueryBuilder buildQuery(RequestParams params, SearchRequest request){
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        //准备DSL
        String key=params.getKey();
        if(key == null || "".equals(key)){
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }else{
            boolQueryBuilder.must(QueryBuilders.matchQuery("all",key));
        }
        String brand=params.getBrand();
        if(brand==null ||"".equals(brand)){
        }else{
            boolQueryBuilder.filter(QueryBuilders.termQuery("brand",brand));
        }
        String city=params.getCity();
        if(city==null ||"".equals(city)){
        }else{
            boolQueryBuilder.filter(QueryBuilders.termQuery("city", city));
        }
        String starName=params.getStarName();
        if(starName==null ||"".equals(starName)){
        }else{
            boolQueryBuilder.filter(QueryBuilders.termQuery("starName",starName));
        }
        Integer maxPrice =params.getMaxPrice();
        Integer minPrice =params.getMinPrice();
        if(maxPrice==null|| "".equals(maxPrice)){
        }else{
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(maxPrice));
        }
        if(minPrice==null || "".equals(minPrice)){
        }else{
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(minPrice));
        }
        //Function score
//         参数   ScoreFunctionBuilder function
        FunctionScoreQueryBuilder functionScoreQueryBuilder= QueryBuilders.functionScoreQuery(boolQueryBuilder,new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.termQuery("isAD",true),
                        ScoreFunctionBuilders.weightFactorFunction(10)
                )
        });
        return functionScoreQueryBuilder;
    }
    private PageResult handleResponse(SearchResponse response){
        //解析响应
        SearchHits searchHits=response.getHits();
        Long total=searchHits.getTotalHits().value;
        SearchHit[]hits=searchHits.getHits();
        List<HotelDoc>hotels=new ArrayList<>();
        for(SearchHit hit:hits){
            String source=hit.getSourceAsString();
            HotelDoc hotelDoc=JSON.parseObject(source,HotelDoc.class);
            if(hit.getSortValues().length!=0){
                hotelDoc.setDistance(hit.getSortValues()[0]);
            }
            hotels.add(hotelDoc);
        }
        return new PageResult(total,hotels);
    }
}
