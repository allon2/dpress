<template>
  <div>
    <a-row
      type="flex"
      justify="center"
      align="middle"
      style="height: 100vh;"
    >
      <a-col
        :xl="8"
        :md="12"
        :sm="20"
        :xs="24"
      >
        <div class="card-container">
          <a-card
            :bordered="false"
            title="Dpress 安装向导"
            style="box-shadow: 0px 10px 20px 0px rgba(236, 236, 236, 0.86);"
          >

            <a-steps :current="stepCurrent">
              <a-step title="数据库信息">
              </a-step>
              <a-step title="博主信息">
              </a-step>
              <a-step title="博客信息">
              </a-step>

            </a-steps>
            <a-divider dashed />
            <!-- DB info -->

            <a-form
              :form="dbForm"
              layout="horizontal"
              v-show="stepCurrent == 0"
            >
              <a-form-item class="animated fadeInUp">
                <a-input
                  v-model="installation.dbhost"
                  placeholder="Host"
                >
                  <a-icon
                    slot="prefix"
                    type="link"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
              <a-form-item
                class="animated fadeInUp"
                :style="{'animation-delay': '0.2s'}"
              >
                <a-input
                  v-model="installation.dbport"
                  placeholder="Port"
                >
                  <a-icon
                    slot="prefix"
                    type="book"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
              <a-form-item
                class="animated fadeInUp"
                :style="{'animation-delay': '0.2s'}"
              >
                <a-input
                  v-model="installation.dbname"
                  placeholder="DbName"
                >
                  <a-icon
                    slot="prefix"
                    type="book"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
              <a-form-item
                class="animated fadeInUp"
                :style="{'animation-delay': '0.2s'}"
              >
                <a-input
                  v-model="installation.dbusername"
                  placeholder="DbName"
                >
                  <a-icon
                    slot="prefix"
                    type="book"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
              <a-form-item
                class="animated fadeInUp"
                :style="{'animation-delay': '0.2s'}"
              >
                <a-input
                  type="password"

                  v-model="installation.dbpassword"
                  placeholder="Password"
                >
                  <a-icon
                    slot="prefix"
                    type="book"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
            </a-form>
            <!-- DB info -->
            <!-- Blogger info -->
            <a-form
              layout="horizontal"
              v-show="stepCurrent == 1"
              :form="bloggerForm"
            >
              <a-form-item class="animated fadeInUp">
                <a-input
                  v-model="installation.username"
                  placeholder="用户名"
                  v-decorator="[
                    'username',
                    {rules: [{ required: true, message: '请输入用户名' }]}
                  ]"
                >
                  <a-icon
                    slot="prefix"
                    type="user"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
              <a-form-item
                class="animated fadeInUp"
                :style="{'animation-delay': '0.1s'}"
              >
                <a-input
                  v-model="installation.nickname"
                  placeholder="用户昵称"
                >
                  <a-icon
                    slot="prefix"
                    type="smile"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
              <a-form-item
                class="animated fadeInUp"
                :style="{'animation-delay': '0.2s'}"
              >
                <a-input
                  v-model="installation.email"
                  placeholder="用户邮箱"
                  v-decorator="[
                    'email',
                    {rules: [{ required: true, message: '请输入邮箱' }]}
                  ]"
                >
                  <a-icon
                    slot="prefix"
                    type="mail"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
              <a-form-item
                class="animated fadeInUp"
                :style="{'animation-delay': '0.3s'}"
              >
                <a-input
                  v-model="installation.password"
                  type="password"
                  placeholder="用户密码（8-100位）"
                  v-decorator="[
                    'password',
                    {rules: [{ required: true, message: '请输入密码（8-100位）' }]}
                  ]"
                >
                  <a-icon
                    slot="prefix"
                    type="lock"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
              <a-form-item
                class="animated fadeInUp"
                :style="{'animation-delay': '0.4s'}"
              >
                <a-input
                  v-model="installation.confirmPassword"
                  type="password"
                  placeholder="确定密码"
                  v-decorator="[
                    'confirmPassword',
                    {rules: [{ required: true, message: '请确定密码' }]}
                  ]"
                >
                  <a-icon
                    slot="prefix"
                    type="lock"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
            </a-form>

            <!-- Blog info -->

            <a-form
              layout="horizontal"
              v-show="stepCurrent == 2"
            >
              <a-form-item class="animated fadeInUp">
                <a-input
                  v-model="installation.url"
                  placeholder="博客预览地址"
                >
                  <a-icon
                    slot="prefix"
                    type="link"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
              <a-form-item
                class="animated fadeInUp"
                :style="{'animation-delay': '0.2s'}"
              >
                <a-input
                  v-model="installation.title"
                  placeholder="博客标题"
                >
                  <a-icon
                    slot="prefix"
                    type="book"
                    style="color: rgba(0,0,0,.25)"
                  />
                </a-input>
              </a-form-item>
            </a-form>

            <a-row
              class="install-action"
              type="flex"
              justify="space-between"
              style="margin-top: 1rem;"
            >
              <div>
                <a-button
                  class="previus-button"
                  v-if="stepCurrent != 0"
                  @click="stepCurrent--"
                  style="margin-right: 1rem;"
                >上一步</a-button>
                <a-button
                  type="primary"
                  v-if="stepCurrent != 2"
                  @click="handleNextStep"
                >下一步</a-button>
              </div>
              <a-button
                v-if="stepCurrent == 2"
                type="primary"
                icon="upload"
                @click="handleInstall"
              >安装</a-button>
            </a-row>
          </a-card>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import adminApi from '@/api/admin'
import migrateApi from '@/api/migrate'

export default {
  data() {
    return {
      installation: {},
      stepCurrent: 0,
      migrationData: null,
      bloggerForm: this.$form.createForm(this),
      dbForm: this.$form.createForm(this)

    }
  },
  created() {
    this.verifyIsInstall()
    this.$set(this.installation, 'url', window.location.protocol + '//' + window.location.host)
  },
  methods: {
    verifyIsInstall() {
      adminApi.isInstalled().then(response => {
        if (response.data.data) {
          this.$router.push({ name: 'Login' })
        }
      })
    },
    handleNextStep(e) {
      e.preventDefault()
      this.stepCurrent++
      // this.bloggerForm.validateFields((error, values) => {
      //   this.$log.debug('error', error)
      //   this.$log.debug('Received values of form: ', values)
      //   if (error != null) {
      //   } else {
      //     this.stepCurrent++
      //   }
      // })
    },
    handleMigrationUpload(data) {
      this.$log.debug('Selected data', data)
      this.migrationData = data
      return new Promise((resolve, reject) => {
        this.$log.debug('Handle uploading')
        resolve()
      })
    },
    install() {
      adminApi.install(this.installation).then(response => {
        this.$log.debug('Installation response', response)
        this.$message.success('安装成功！')
        setTimeout(() => {
          this.$router.push({ name: 'Login' })
        }, 300)
      })
    },
    handleInstall() {
      const password = this.installation.password
      const confirmPassword = this.installation.confirmPassword

      this.$log.debug('Password', password)
      this.$log.debug('Confirm password', confirmPassword)

      if (password !== confirmPassword) {
        this.$message.error('确认密码和密码不匹配')
        return
      }

      if (this.migrationData) {
        const hide = this.$message.loading('数据导入中...', 0)
        migrateApi
          .migrate(this.migrationData)
          .then(response => {
            this.$log.debug('Migrated successfullly')
            this.$message.success('数据导入成功！')
            this.install()
          })
          .finally(() => {
            hide()
          })
      } else {
        this.install()
      }
    }
  }
}
</script>
