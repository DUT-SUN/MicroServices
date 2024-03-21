package cn.itcast.hotel.constants;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/19  16:26
 */
public class HotelConstant {
    public static final String MappingTemplate="{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\":\"text\",\n" +
            "       \"analyzer\": \"ik_max_word\",\n" +
            "       \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"address\":{\n" +
            "        \"type\":\"keyword\",\n" +
            "        \"index\":false\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "         \"score\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"brand\":{\n" +
            "        \"type\":\"keyword\",\n" +
            "         \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"city\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "        \"starName\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "        \"businesss\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "         \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"location\":{\n" +
            "        \"type\":\"geo_point\"\n" +
            "      },\n" +
            "           \"pic\":{\n" +
            "        \"type\":\"keyword\",\n" +
            "        \"index\":false\n" +
            "      },\n" +
            "         \"all\":{\n" +
            "        \"type\":\"text\",\n" +
            "       \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
