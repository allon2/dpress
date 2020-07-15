package run.halo.app.model.properties;

import run.halo.app.model.support.HaloConst;

/**
 * Baidu bos properties.
 *
 * @author wangya
 * @author ryanwang
 * @date 2019-07-19
 */
public enum BaiduZiyuanProperties implements PropertyEnum {

    BaiduZiyuan_DISABLED("baiduziyuan_disabled", Boolean.class, "false"),



    /**
     * Baidu bos domain.
     */
    BaiduZiyuan_API("baiduziyuan_api", String.class, ""),

    /**
     * Baidu bos endpoint.
     */
    BaiduZiyuan_DAYSIZE("baiduziyuan_daysize", Integer.class, "10000"),

    GoogleAdsense_ADS("googleadsense_ads", String.class, "");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    BaiduZiyuanProperties(String value, Class<?> type, String defaultValue) {
        this.defaultValue = defaultValue;
        if (!PropertyEnum.isSupportedType(type)) {
            throw new IllegalArgumentException("Unsupported blog property type: " + type);
        }

        this.value = value;
        this.type = type;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public String getValue() {
        return value;
    }
}
