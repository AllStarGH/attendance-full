<template>
  <el-header>
    <el-row>
      <div>
        <span class="head_span">
          <el-link icon="el-icon-user-solid" @click="replacesTo">
            {{viewName}}</el-link>
        </span>
        <span class="head_span">
          <el-link href="/attendance" title="返回首页" icon="el-icon-s-home">返回首页</el-link>
        </span>
      </div>
    </el-row>
  </el-header>
</template>
<script>
export default {
  name: "UserHeader",
  data() {
    return {

    }
  },
  methods: {
    replacesTo() {
      this.$router.replace(this.url);
    }
  },
  props: {// 子组件中的所有props中的数据,都是通过父组件传递给子组件的,是只读的
    url: {
      type: String,
      default: "",
    },
    viewName: {
      type: String,
      default: "",
    }
  },
  watch: {

  },
  mounted: function() {
    console.log(this)
  },
}

</script>
<style>
.el-header,
.el-footer {
  background-color: #66b1ff;
  color: #fbfbfb;
  text-align: right;
  line-height: 60px;
}

.el-link {
  font-size: 18px;
}

.el-link.el-link--default {
  color: #fffdfd;
}

.head_span {
  margin: 0 15px;
}

</style>
