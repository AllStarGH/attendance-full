<template>
  <div id="app">
    <!-- <img src="./assets/logo.png"> -->
    <!-- <router-view/> -->
    <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>
<script>
export default {
  name: 'App',
  provide() {
    return {
      reload: this.reload,
    }
  },
  data() {
    return {
      isRouterAlive: true,
    }
  },
  methods: {
    reload() {
      this.isRouterAlive = false;
      // this.$nextTick(function() {
      //   this.isRouterAlive = true;
      // })
      this.$nextTick(() => (this.isRouterAlive = true))
    }
  },
  mounted: function() {
    console.info(this);
  },
}

</script>
<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*text-align: center;*/
  color: #2c3e50;
  /*margin-top: 60px;*/
}

</style>
