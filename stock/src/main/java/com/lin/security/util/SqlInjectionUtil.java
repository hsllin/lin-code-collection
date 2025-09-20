package com.lin.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * SQL注入防护工具类
 * 
 * @author lin
 */
public class SqlInjectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(SqlInjectionUtil.class);

    // SQL注入攻击模式
    private static final Pattern[] SQL_INJECTION_PATTERNS = {
        // 注释符号
        Pattern.compile("--", Pattern.CASE_INSENSITIVE),
        Pattern.compile("/\\*.*?\\*/", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
//        Pattern.compile("#", Pattern.CASE_INSENSITIVE),
        
        // 联合查询
        Pattern.compile("union\\s+select", Pattern.CASE_INSENSITIVE),
        Pattern.compile("union\\s+all\\s+select", Pattern.CASE_INSENSITIVE),
        
        // 查询语句
        Pattern.compile("select\\s+.*?\\s+from", Pattern.CASE_INSENSITIVE),
        Pattern.compile("insert\\s+into", Pattern.CASE_INSENSITIVE),
        Pattern.compile("update\\s+.*?\\s+set", Pattern.CASE_INSENSITIVE),
        Pattern.compile("delete\\s+from", Pattern.CASE_INSENSITIVE),
        Pattern.compile("drop\\s+table", Pattern.CASE_INSENSITIVE),
        Pattern.compile("drop\\s+database", Pattern.CASE_INSENSITIVE),
        Pattern.compile("create\\s+table", Pattern.CASE_INSENSITIVE),
        Pattern.compile("create\\s+database", Pattern.CASE_INSENSITIVE),
        Pattern.compile("alter\\s+table", Pattern.CASE_INSENSITIVE),
        
        // 函数调用
        Pattern.compile("\\b(exec|execute|sp_|xp_)\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\b(load_file|into\\s+outfile|into\\s+dumpfile)\\b", Pattern.CASE_INSENSITIVE),
        
        // 系统函数
        Pattern.compile("\\b(user|database|version|schema|table_name|column_name)\\s*\\(", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\b(concat|substring|ascii|char|hex|unhex)\\s*\\(", Pattern.CASE_INSENSITIVE),
        
        // 布尔盲注
        Pattern.compile("\\b(and|or)\\s+\\d+\\s*=\\s*\\d+", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\b(and|or)\\s+\\d+\\s*like\\s+\\d+", Pattern.CASE_INSENSITIVE),
        
        // 时间盲注
        Pattern.compile("\\b(and|or)\\s+\\d+\\s*=\\s*\\d+\\s*and\\s+sleep\\s*\\(", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\b(and|or)\\s+\\d+\\s*=\\s*\\d+\\s*and\\s+benchmark\\s*\\(", Pattern.CASE_INSENSITIVE),
        
        // 堆叠查询
        Pattern.compile(";\\s*(select|insert|update|delete|drop|create|alter)", Pattern.CASE_INSENSITIVE),
        
        // 特殊字符
//        Pattern.compile("['\";\\\\]", Pattern.CASE_INSENSITIVE),
        
        // 十六进制编码
        Pattern.compile("0x[0-9a-fA-F]+", Pattern.CASE_INSENSITIVE),
        
        // 宽字节注入
        Pattern.compile("%df%27", Pattern.CASE_INSENSITIVE),
        
        // 二次编码
        Pattern.compile("%25[0-9a-fA-F]{2}", Pattern.CASE_INSENSITIVE)
    };

    // 危险关键词
    private static final String[] DANGEROUS_KEYWORDS = {
        "union", "select", "insert", "update", "delete", "drop", "create", "alter",
        "exec", "execute", "sp_", "xp_", "load_file", "into outfile", "into dumpfile",
        "user()", "database()", "version()", "schema()", "table_name", "column_name",
        "concat(", "substring(", "ascii(", "char(", "hex(", "unhex(",
        "sleep(", "benchmark(", "waitfor", "delay",
        "information_schema", "mysql", "sys", "performance_schema"
    };

    /**
     * 检查字符串是否包含SQL注入攻击
     */
    public static boolean containsSqlInjection(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // 检查SQL注入模式
        for (Pattern pattern : SQL_INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                logger.debug("检测到SQL注入模式: {}", pattern.pattern());
                return true;
            }
        }

        // 检查危险关键词
        String lowerInput = input.toLowerCase();
        for (String keyword : DANGEROUS_KEYWORDS) {
            if (lowerInput.contains(keyword.toLowerCase())) {
                logger.debug("检测到危险关键词: {}", keyword);
                return true;
            }
        }

        return false;
    }

    /**
     * 清理SQL注入内容
     */
    public static String cleanSqlInjection(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        try {
            // 转义单引号
            String cleaned = input.replace("'", "''");
            
            // 转义双引号
            cleaned = cleaned.replace("\"", "\\\"");
            
            // 转义反斜杠
            cleaned = cleaned.replace("\\", "\\\\");
            
            // 移除注释符号
            cleaned = cleaned.replace("--", "");
            cleaned = cleaned.replace("/*", "");
            cleaned = cleaned.replace("*/", "");
            cleaned = cleaned.replace("#", "");
            
            // 移除分号
            cleaned = cleaned.replace(";", "");
            
            return cleaned;
        } catch (Exception e) {
            logger.error("清理SQL注入内容时发生错误", e);
            return "";
        }
    }

    /**
     * 验证输入是否为有效的SQL标识符
     */
    public static boolean isValidSqlIdentifier(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        
        // SQL标识符只能包含字母、数字、下划线和美元符号
        return input.matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$");
    }

    /**
     * 验证输入是否为有效的表名
     */
    public static boolean isValidTableName(String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            return false;
        }
        
        // 表名长度限制
        if (tableName.length() > 64) {
            return false;
        }
        
        // 表名格式验证
        return tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    /**
     * 验证输入是否为有效的列名
     */
    public static boolean isValidColumnName(String columnName) {
        if (columnName == null || columnName.isEmpty()) {
            return false;
        }
        
        // 列名长度限制
        if (columnName.length() > 64) {
            return false;
        }
        
        // 列名格式验证
        return columnName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    /**
     * 验证输入是否为有效的数字
     */
    public static boolean isValidNumber(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        
        try {
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证输入是否为有效的整数
     */
    public static boolean isValidInteger(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证输入是否为有效的浮点数
     */
    public static boolean isValidFloat(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证输入是否为有效的日期格式
     */
    public static boolean isValidDate(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        
        // 简单的日期格式验证 (YYYY-MM-DD)
        return input.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    /**
     * 验证输入是否为有效的时间格式
     */
    public static boolean isValidTime(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        
        // 简单的时间格式验证 (HH:MM:SS)
        return input.matches("^\\d{2}:\\d{2}:\\d{2}$");
    }

    /**
     * 验证输入是否为有效的日期时间格式
     */
    public static boolean isValidDateTime(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        
        // 简单的日期时间格式验证 (YYYY-MM-DD HH:MM:SS)
        return input.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");
    }

    /**
     * 验证输入长度
     */
    public static boolean isValidLength(String input, int maxLength) {
        if (input == null) {
            return true;
        }
        return input.length() <= maxLength;
    }

    /**
     * 验证输入是否包含特殊字符
     */
    public static boolean containsSpecialCharacters(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        
        // 检查是否包含SQL特殊字符
        return input.matches(".*['\";\\\\<>].*");
    }
}
