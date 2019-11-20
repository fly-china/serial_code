package com.lpf.common.page.plugin.dialect;

/**
 * 分页插件Oracle方言
 * User: 韩彦伟
 * Date: 14-7-31
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */
public class OracleDialect extends Dialect{

    public boolean supportsLimitOffset(){
        return true;
    }

    public boolean supportsLimit() {
        return true;
    }

    public String getLimitString(String sql, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
        int endIndex = 0;

        //offset本来是从0开始的，而数据库是从1开始的，适配下
        offset = offset + 1;

        endIndex = offset + limit -1;

        return " SELECT * " +
                "  FROM (SELECT tt.*, ROWNUM AS rowno" +
                "          FROM ("+sql+") tt" +
                "         WHERE ROWNUM <= "+endIndex+") table_alias" +
                "  WHERE table_alias.rowno >= "+offset;
    }
}
