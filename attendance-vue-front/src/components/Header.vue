<template>
  <div class="container">
    <el-header class="app-header" height="60px">
      <!-- <div > -->
      <div class="app-side-logo">
        <img src="@/assets/logo.png" :width="isCollapse?'60':'60'" height="60" />
      </div>
      <!-- -->
      <el-row>
        <div>
          <el-menu default-active="1" class="el-list-item el-menu-demo tab-page" mode="horizontal" active-text-color="#409EFF">
            <template>
              <el-menu-item index="0" class="_title">
                <el-link href="/attendance/regist">注册</el-link>
              </el-menu-item>
              <!--  -->
              <el-menu-item index="1" class="_title">
                <el-link href="/attendance/login">登录</el-link>
              </el-menu-item>
              <!--  -->
              <el-menu-item index="2" class="_title">
                <el-link href="#">关于本站</el-link>
              </el-menu-item>
            </template>
          </el-menu>
        </div>
      </el-row>
    </el-header>
  </div>
</template>
<script>
export default {
  name: "Header",
  data() {
    return {
      isCollapse: false
    }
  }
}

</script>
<style scoped>
.app-side-logo {
  width: 60px;
  cursor: pointer;
}

.app-header {
  border-bottom: 2px solid #eee;
}

.el-row {
  left: 84%;
  width: 100%;
}

</style>
