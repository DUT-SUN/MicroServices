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
    public static final String MappingTemplate="\n" +
            "  \"settings\": {\n" +
            "      \"index\": {\n" +
            "      \"number_of_shards\": 3\n" +
            "      },\n" +
            "    \"analysis\": {\n" +
            "      \"analyzer\": {\n" +
            "        \"text_anlyzer\": {\n" +
            "          \"tokenizer\": \"ik_max_word\",\n" +
            "          \"filter\": \"py\"\n" +
            "        },\n" +
            "        \"completion_analyzer\": {\n" +
            "          \"tokenizer\": \"keyword\",\n" +
            "          \"filter\": \"py\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"filter\": {\n" +
            "        \"py\": {\n" +
            "          \"type\": \"pinyin\",\n" +
            "          \"keep_full_pinyin\": false,\n" +
            "          \"keep_joined_full_pinyin\": true,\n" +
            "          \"keep_original\": true,\n" +
            "          \"limit_first_letter_length\": 16,\n" +
            "          \"remove_duplicated_term\": true,\n" +
            "          \"none_chinese_pinyin_tokenize\": false\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"uid\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"username\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"text_anlyzer\",\n" +
            "        \"search_analyzer\": \"ik_smart\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"title\":{\n" +
            "       \"type\": \"text\",\n" +
            "       \"analyzer\": \"text_anlyzer\",\n" +
            "       \"search_analyzer\": \"ik_smart\",\n" +
            "       \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"content\":{\n" +
            "       \"type\": \"text\",\n" +
            "       \"analyzer\": \"ik_smart\",\n" +
            "       \"search_analyzer\": \"ik_smart\",\n" +
            "       \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"state\":{\n" +
            "        \"type\":\"integer\"\n" +
            "      },\n" +
            "      \"favorite\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"comment\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"text_anlyzer\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"suggestion\":{\n" +
            "          \"type\": \"completion\",\n" +
            "          \"analyzer\": \"completion_analyzer\"\n" +
            "      }\n" +
            "    }\n" +
            "  }";
}
