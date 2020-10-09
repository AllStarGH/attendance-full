<template>
  <div class="app">
    <el-container>
      <el-aside id="el_side0" class="app-side app-side-left" :class="isCollapse?'app-side-collapsed':'app-side-expanded'">
        <div class="app-side-logo">
          <img src="@/assets/logo.png" :width="isCollapse?'60':'60'" height="60" />
        </div>
        <div>
          <!-- 我是样例菜单 -->
          <el-menu default-active="/" router class="el-menu-demo tab-page" @select="handleSelect" active-text-color="#409eff">
            <el-menu-item index="/">首页</el-menu-item>
          </el-menu>
          <!--  -->
          <Sidebar :collapse="isCollapse" :routes="$router.options.routes[4].children" />
          <!--  -->
          <el-menu :default-active="defaultActive" router class="el-menu-vertical-demo" @open="handleOpen" :collapse="isCollapse">
            <template v-for="route in $router.options.routes" v-if="route.children && route.children.length">
              <template v-for="item in route.children">
                <el-menu-item :key="route.path+'/' + item.path" :index="item.path">
                  <i class="el-icon-user"></i>
                  <span slot="title">{{item.name}}</span>
                </el-menu-item>
              </template>
            </template>
          </el-menu>
          <!--  -->
          <el-menu default-active="1-4-1" class="el-menu-vertical-demo" @open="handleOpen" :collapse="isCollapse">
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-location"></i>
                <span slot="title">导航壹</span>
              </template>
              <el-menu-item-group>
                <span slot="title">分组壹</span>
                <el-menu-item index="1-0">选项零</el-menu-item>
                <el-menu-item index="1-1">选项贰</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group>
                <span slot="title">分组贰</span>
                <el-menu-item index="1-2">选项叁</el-menu-item>
              </el-menu-item-group>
              <el-submenu index="1-4">
                <span slot="title">选项肆</span>
                <el-menu-item index="1-4-1">选项壹</el-menu-item>
              </el-submenu>
            </el-submenu>
            <!--  -->
            <el-submenu index="5">
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span slot="title">导航贰</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="2-0-1">选项贰零壹</el-menu-item>
                <el-menu-item index="2-0-2">选项贰零贰</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </div>
      </el-aside>
      <el-container>
        <el-header class="app-header">
          <div class="div_app_header" @click.prevent="toggleSideBar">
            <i v-show="!isCollapse" class="el-icon-d-arrow-left"></i>
            <i v-show="isCollapse" class="el-icon-d-arrow-right"></i>
          </div>
          <!-- 我是样例菜单 -->
          <el-menu default-active="1" class="el-menu-demo tab-page" mode="horizontal" @select="handleSelect" active-text-color="#409EFF">
            <el-menu-item index="1">处理中心</el-menu-item>
            <el-submenu index="2">
              <template slot="title">我的工作台</template>
              <el-menu-item>选项1</el-menu-item>
              <el-menu-item>选项2</el-menu-item>
              <el-menu-item>选项3</el-menu-item>
            </el-submenu>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item>选项1</el-menu-item>
              <el-menu-item>选项2</el-menu-item>
              <el-menu-item>选项3</el-menu-item>
            </el-submenu>
            <el-menu-item index="3">消息中心</el-menu-item>
            <!--  -->
            <el-submenu index="4">
              <template slot="title">选项004</template>
              <el-menu-item>选项004-1</el-menu-item>
              <el-menu-item>选项004-2</el-menu-item>
              <el-menu-item>选项004-3</el-menu-item>
            </el-submenu>
            <!--  -->
          </el-menu>
          <div class="usrname">
            <el-dropdown>
              <i class="el-icon-setting" id="i_setting"></i>
              <el-dropdown-menu>
                <el-dropdown-item>查看</el-dropdown-item>
                <el-dropdown-item>新增</el-dropdown-item>
                <el-dropdown-item>修改</el-dropdown-item>
                <el-dropdown-item>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <b>{{accEmployeeName}}</b>
          </div>
          <div class="avatar demo-image">
            <div class="block img0div0">
              <el-image :src="img_url" class="img0">
              </el-image>
            </div>
          </div>
        </el-header>
        <el-main class="app-body">
          <template>
            <router-view />
          </template>
        </el-main>
        <!--  -->
      </el-container>
    </el-container>
  </div>
</template>
<script>
import Sidebar from '@/components/Sidebar'
export default {
  name: "Admin",
  components: {
    Sidebar,
  },
  data() {
    return {
      accEmployeeName: sessionStorage.getItem('accEmployeeName'),
      isCollapse: false,
      defaultActive: "defaultActive",
      img_url: sessionStorage.getItem('accPortrait'),
    }
  },
  methods: {
    jumpToLogin() {
      this.$router.push("/login");
    },
    toggleSideBar() {
      this.isCollapse = !this.isCollapse;
    },
    loginOut: function() {
      this.confirm("确认退出?", "提示", {})
        .then(() => {
          sessionStorage.removeItem("accPhone");
          this.$router.push("/login");
        })
        .catch(() => {});
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    }
  },
  mounted: function() {
    console.log(this);
  }
}

</script>
<style scoped lang="scss">
.usrname b {
  padding: 0 0 0 1em;
}

.usrname {
  font-size: 14px;
  color: #909399;
  padding: 0 0 0 1rem;
  transform: translate(27rem, 0px);
}

._title {
  color: #42454b;
}

.container {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 100%;

  .header {
    height: 60px;
    line-height: 60px;
    background: #545c64;
    color: #fff;
    padding: 0px;

    border-bottom: {
      width: 1px;
      color: #fff;
      style: solid;
    }

    .logo {
      height: 60px;
      font-size: 30px;

      padding: {
        left: 15px;
        right: 15px;
      }

      border: {
        color: #fff;

        right: {
          width: 1px;
          style: solid;
        }
      }
    }

    .logo-width {
      width: 230px;
    }

    .logo-collapsed {
      width: 64px;
    }

    .collapse {
      padding: 0 25px;
      width: 14px;
      height: 64px;
      line-height: 64px;
      cursor: pointer;
    }

    .userinfo {
      text-align: right;
      padding-right: 35px;
      color: #fff;

      .el-dropdown-link {
        cursor: pointer;
        color: #fff;
      }
    }
  }

  .main {
    display: flex;
    position: absolute;
    top: 60px;
    bottom: 0;
    overflow: hidden;

    aside {
      background: rgb(240, 240, 240);
      flex: 0 0 230px;
      width: 230px;

      .el-menu {
        height: 100%;
      }

      .collapsed {
        width: 64px;

        .item {
          position: relative;
        }

        .submenu {
          position: absolute;
          top: 0;
          left: 64px;
          height: auto;
          z-index: 999;
          display: none;
        }
      }
    }

    .aside-collapsed {
      flex: 0 0 64px;
      width: 64px !important;
    }

    .aside-expanded {
      flex: 0 0 230px;
      width: 230px;
    }

    .content-container {
      flex: 1;
      overflow-y: scroll;

      .breadcrumb-container {
        padding-left: 5px;
        background-color: #fff;
        color: #aaa;

        border: {
          bottom: 5px;
          color: rgb(240, 240, 240);
          style: solid;
        }
      }

      .content-wrapper {
        border: 1px solid rgb(240, 240, 240);
        padding: 20px;
        background: #fff;
        box-sizing: border-box;
      }
    }
  }
}

#el_side0 {
  height: 1500px;
}

.img0div0 {
  transform: translate(20em, 0px);
}

.img0 {
  cursor: pointer;
  width: 65px;
  height: 42px;
  transform: translate(-45px, 0px);
}

.div_app_header {
  width: 60px;
  cursor: pointer;
}

</style>
