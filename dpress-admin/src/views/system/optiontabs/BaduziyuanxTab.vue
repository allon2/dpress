<template>
  <div>
    <a-form
      layout="vertical"
      :wrapperCol="wrapperCol"
    >
      <a-form-item label="开启百度站长索引提交：">
        <a-switch v-model="options.baiduziyuan_disabled" />
      </a-form-item>
      <a-form-item label="百度站长每天提交条数：">
        <a-input
          type="number"
          v-model="options.baiduziyuan_daysize"
        />
      </a-form-item>
      <a-form-item label="百度站长API：">
        <a-input
          v-model="options.baiduziyuan_api"
          placeholder="api"
        />
      </a-form-item>
 <a-form-item label="Google Adsense Ads.txt：">
        <a-input
          v-model="options.googleadsense_ads"
          placeholder="google.com, pub-0000000000000000, DIRECT, f08c47fec0942fa0"
        />
      </a-form-item>
      <a-form-item>
        <a-button
          type="primary"
          @click="handleSaveOptions"
        >保存</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script>
export default {
  name: 'SeoTab',
  props: {
    options: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      wrapperCol: {
        xl: { span: 8 },
        lg: { span: 8 },
        sm: { span: 12 },
        xs: { span: 24 }
      }
    }
  },
  watch: {
    options(val) {
      this.$emit('onChange', val)
    }
  },
  methods: {
    handleSaveOptions() {
      this.$emit('onSave')
    }
  }
}
</script>
