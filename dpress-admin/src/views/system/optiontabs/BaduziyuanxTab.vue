<template>
  <div>
    <a-form
      layout="vertical"
      :wrapperCol="wrapperCol"
    >
      <a-form-item label="开启百度索引提交：">
        <a-switch v-model="options.baiduziyuan_disabled" />
      </a-form-item>
      <a-form-item label="每天提交条数：">
        <a-input
          type="number"
          v-model="options.baiduziyuan_daysize"
        />
      </a-form-item>
      <a-form-item label="API：">
        <a-input
          v-model="options.baiduziyuan_api"
          placeholder="api"
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
