<template>
  <div>
    <el-menu class="el-menu-vertical-demo" :default-active="defaultActive" router :collapse="collapse">
      <SidebarItem v-for="(item,index) in routes" :subroute="item" :superPath="superPath" :barIdx="index" :key="index" />
    </el-menu>
  </div>
</template>
<script>
import SidebarItem from './SidebarItem'
export default {
  name: 'Sidebar',
  components: {
    SidebarItem
  },
  props: {
    collapse: {
      type: Boolean
    },
    routes: {
      type: Array
    },
  },
  computed: {
    //初次进入页面时展开当前页面所属的菜单
    defaultActive() {
      return this.$route.path
    },
    //直接获取路由配置的'/'项
    superPath() {
      return this.$router.options.routes[4].path
    }
  }
}

</script>
