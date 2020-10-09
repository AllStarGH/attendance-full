<template>
  <div class="div_introd">
    <div class="h2div">
      <h2>
        Attendance考勤签到系统介绍
      </h2>
    </div>
    <div class="hr00div11">
      <hr>
    </div>
    <div class="div0001">
      <div class="p0p00div">
        <p class="div00pre">
          <p class="p0pre1">
            本系统主要为企业提供员工考勤签到记录服务,包括上下班打卡/请假/加班/补休等功能.
          </p>
          <p class="p0pre1">
            本系统采用前后端分离开发模式进行开发,分为前端+后台两大部分.
          </p>
        </p>
      </div>
    </div>
    <div>
      <div class="ol0div0strong">
        <div>
          <ol>
            <div class="strongs11">
              <strong>
                前端技术
              </strong>
            </div>
            <li>
              MVVM框架：Vue.JS
            </li>
            <li>
              路由：Vue-Router
            </li>
            <li>
              UI框架：Element-UI
            </li>
            <li>
              交互: axios
            </li>
            <li>
              项目构建工具：webpack
            </li>
          </ol>
        </div>
        <div>
          <ol>
            <div class="strongs11">
              <strong>
                后台技术
              </strong>
            </div>
            <li>
              核心框架: SpringBoot
            </li>
            <li>
              项目构建工具：maven
            </li>
            <li>
              Web容器：tomcat
            </li>
            <li>
              JDK: jdk1.8
            </li>
          </ol>
        </div>
      </div>
    </div>
  </div>
</template>
<!--  -->
<script>
export default {
  name: 'Introd',
  data() {
    return {
      
    }
  },
  mounted() {
    console.log(this)
  }
}

</script>
<!--  -->
<style scoped>
div {
  color: #848484;
}

.div_introd {
  width: 100%;
}

.h2div {
  text-align: center;
}

.p0pre1 {
  margin-block-end: 8px;
  margin-block-start: 8px;
}

.div00pre {
  margin: 10px 0;
}

.ol0div0strong {
  transform: translate(30%, 58px);
  width: 350px;
}

.p0p00div {
  width: 879px;
  transform: translate(13%, 1px);
}

.div0001 {
  margin: 60px 0 0 0;
}

.strongs11 {
  margin-bottom: 12px;
}

</style>
