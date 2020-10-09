<template>
  <div class="container">
    <div>
      <app-header></app-header>
    </div>
    <div class="facade_main">
      <div class="facade_title">
        <h2>{{facadeTitle}}</h2>
      </div>
      <div class="app-side-logo main_logo">
        <img src="@/assets/logo.png" :width="isCollapse?'260':'260'" height="260" />
      </div>
    </div>
  </div>
</template>
<script>
import Header from '../components/Header'
export default {
  name: 'Facade',
  data() {
    return {
      isCollapse: false,
      facadeTitle: ' Attendance OA ',
    }
  },
  components: {
    "app-header": Header
  }
}

</script>
<style>
h1,
h2 {
  font-weight: normal;
}

.main_logo {
  transform: translateY(3rem);
}

.facade_main {
  background-color: #fff;
  text-align: center;
}

.facade_title {
  font-size: 2em;
}

.title_facade {
  text-align: center;
}

</style>
