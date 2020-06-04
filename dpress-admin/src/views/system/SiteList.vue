<template>
  <div>
    <a-row :gutter="12">
      <a-col
        :xl="10"
        :lg="10"
        :md="10"
        :sm="24"
        :xs="24"
        :style="{ 'padding-bottom': '12px' }"
      >
        <a-card
          :title="title"
          :bodyStyle="{ padding: '16px' }"
        >
          <a-form layout="horizontal">
            <a-form-item
              label="站点名称："
              help="* 站点名称"
            >
              <a-input v-model="menuToCreate.sitename" />
            </a-form-item>
            <a-form-item
              label="站点域名"
              help="* 站点所属域名"
            >
              <a-input v-model="menuToCreate.domain" placeholder="dpress.ymotel.cn" />
            </a-form-item>
            <a-form-item
              label="博客标题"
              help="* 博客标题"
            >
              <a-input v-model="menuToCreate.title" placeholder="dpress" />
            </a-form-item>
            <a-form-item
              label="博客预览地址"
              help="* 博客预览地址"
            >
              <a-input v-model="menuToCreate.url" placeholder="http://dpress.ymotel.cn" />
            </a-form-item>
            <a-form-item>
              <a-button
                type="primary"
                @click="handleSaveClick"
                v-if="formType==='create'"
              >保存</a-button>
              <a-button-group v-else>
                <a-button
                  type="primary"
                  @click="handleSaveClick"
                >更新</a-button>
                <a-button
                  type="dashed"
                  @click="handleAddMenu"
                  v-if="formType==='update'"
                >返回添加</a-button>
              </a-button-group>
            </a-form-item>
          </a-form>
        </a-card>
      </a-col>
      <a-col
        :xl="14"
        :lg="14"
        :md="14"
        :sm="24"
        :xs="24"
        :style="{ 'padding-bottom': '12px' }"
      >
        <a-card
          title="所有站点"
          :bodyStyle="{ padding: '16px' }"
        >
          <!-- Mobile -->
          <a-list
            v-if="isMobile()"
            itemLayout="vertical"
            size="large"
            :pagination="false"
            :dataSource="menus"
            :loading="loading"
          >
            <a-list-item
              slot="renderItem"
              slot-scope="item, index"
              :key="index"
            >
              <template slot="actions">
                <a-dropdown
                  placement="topLeft"
                  :trigger="['click']"
                >
                  <span>
                    <a-icon type="bars" />
                  </span>
                  <a-menu slot="overlay">
                    <a-menu-item>
                      <a
                        href="javascript:;"
                        @click="handleEditMenu(item)"
                      >编辑</a>
                    </a-menu-item>
                    <a-menu-item>
                      <a-popconfirm
                        :title="'你确定要删除【' + item.sitename + '】菜单？'"
                        @confirm="handleDeleteMenu(item.id)"
                        okText="确定"
                        cancelText="取消"
                      >
                        <a href="javascript:;">删除</a>
                      </a-popconfirm>
                    </a-menu-item>

                  </a-menu>
                </a-dropdown>
              </template>
              <template slot="extra">
                <span>
                  {{ item.team }}
                </span>
              </template>
              <a-list-item-meta>
                <template slot="description">
                  {{ item.url }}
                </template>
                <span
                  slot="title"
                  style="max-width: 300px;display: block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;"
                >
                  {{ item.name }}
                </span>
              </a-list-item-meta>
            </a-list-item>
          </a-list>
          <!-- Desktop -->
          <a-table
            v-else
            :columns="columns"
            :dataSource="menus"
            :loading="loading"
            :rowKey="menu => menu.id"
            :scrollToFirstRowOnChange="true"
          >
            <span
              slot="action"
              slot-scope="text, record"
            >
              <a
                href="javascript:;"
                @click="handleEditMenu(record)"
              >编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm
                :title="'你确定要删除【' + record.sitename + '】菜单？'"
                @confirm="handleDeleteMenu(record)"
                okText="确定"
                cancelText="取消"
              >
                <a href="javascript:;">删除</a>
              </a-popconfirm>
            </span>
          </a-table>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { mixin, mixinDevice } from '@/utils/mixin.js'
import MenuSelectTree from './components/MenuSelectTree'
import menuApi from '@/api/site'
const columns = [
  {
    title: '站点名称',
    dataIndex: 'sitename',
    ellipsis: true
  },
  {
    title: '域名',
    ellipsis: true,
    dataIndex: 'domain'
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]
export default {
  components: { MenuSelectTree },
  mixins: [mixin, mixinDevice],
  data() {
    return {
      formType: 'create',
      loading: false,
      columns,
      menus: [],
      menuToCreate: {
        target: '_self'
      },
      fieldExpand: false,
      teams: []
    }
  },
  computed: {
    title() {
      if (this.menuToCreate.id) {
        return '修改站点'
      }
      return '添加站点'
    }
  },
  created() {
    this.loadMenus()
  },
  methods: {
    loadMenus() {
      this.loading = true
      menuApi.listTree().then(response => {
        this.menus = response.data
        this.loading = false
      })
    },
    handleSaveClick() {
      this.createOrUpdateMenu()
    },
    handleAddMenu() {
      this.formType = 'create'
      this.menuToCreate = {}
    },
    handleEditMenu(menu) {
      this.menuToCreate = menu
      this.formType = 'update'
    },
    handleDeleteMenu(id) {
      menuApi.delete(id).then(response => {
        this.$message.success('删除成功！')
        this.loadMenus()
      })
    },
    createOrUpdateMenu() {
      if (!this.menuToCreate.sitename) {
        this.$notification['error']({
          message: '提示',
          description: '站点名称不能为空！'
        })
        return
      }
      if (!this.menuToCreate.domain) {
        this.$notification['error']({
          message: '提示',
          description: '域名不能为空！'
        })
        return
      }
      if (this.menuToCreate.id) {
        menuApi.update(this.menuToCreate.id, this.menuToCreate).then(response => {
          this.$message.success('更新成功！')
          this.loadMenus()
        })
      } else {
        menuApi.create(this.menuToCreate).then(response => {
          this.$message.success('保存成功！')
          this.loadMenus()
        })
      }
      this.handleAddMenu()
    },
    toggleExpand() {
      this.fieldExpand = !this.fieldExpand
    }
  }
}
</script>
