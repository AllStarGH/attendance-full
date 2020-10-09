<template>
  <div class="avatar demo-image">
    <div class="block img0div0">
      <el-avatar shape="square" :src="img_url" :size="50" class="img0">
      </el-avatar>
    </div>
  </div>
</template>
<script>
export default {
  name: 'Avatar',
  data() {
    return {
      // img_url: require("@/assets/vueRoute.jpg"),
      img_url: sessionStorage.getItem('accPortrait'),
    }
  }
}

</script>
<style scoped>
.avatar {
  width: 60px;
  height: 50px;
}

.img0div0 {
  transform: translate(1030%, 9px);
}

.img0 {
  transform: translate(600%, -3px);
  cursor: pointer;
}

.el-avatar>img {
  display: block;
  height: 100%;
  vertical-align: middle;
  width: 50px;
}

</style>
