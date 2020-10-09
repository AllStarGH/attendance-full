<template>
  <el-container>
    <div class="title_div">
      <h3>修改密码</h3>
    </div>
    <!--  -->
    <div>
      <el-form ref="commitForm" :model="commitForm" :rules="commitRules" auto-complete="on" label-position="top">
        <el-form-item prop="oldPassword" label="请输入原密码">
          <el-input v-model="commitForm.oldPassword" autofocus="autofocus" type="password" auto-complete="on" @keyup.enter.navite="handleSubmits" maxlength="16" />
        </el-form-item>
        <!--  -->
        <el-form-item prop="newPassword" label="请输入新密码">
          <el-input v-model="commitForm.newPassword" autofocus="autofocus" type="password" auto-complete="on" @keyup.enter.navite="handleSubmits" maxlength="16" />
        </el-form-item>
        <!--  -->
        <el-form-item prop="reNewPassword" label="请再次输入新密码">
          <el-input v-model="commitForm.reNewPassword" autofocus="autofocus" type="password" auto-complete="on" maxlength="16" />
        </el-form-item>
        <!--  -->
        <el-form-item class="btn_item">
          <el-button class="committed_btn" @click.native.prevent="handleCommits">
            提交
          </el-button>
        </el-form-item>
        <!--  -->
      </el-form>
    </div>
  </el-container>
</template>
<script>
export default {
  inject: ['reload'], //注入依赖
  name: 'AlterKeyWord',
  data() { //初始化数据
    const validateOldPassword = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error('请输入原密码'))
      } else {
        callback()
      }
    };
    const validateNewPassword = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error('请输入新密码'))
      } else {
        callback()
      }
    };
    const validateReNewPassword = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error('请再次输入新密码'))
      } else {
        callback()
      }
    };
    return {
      commitForm: {
        oldPassword: '',
        newPassword: '',
        reNewPassword: '',
      },
      commitRules: {
        oldPassword: [
          { required: true, trigger: 'blur', validator: validateOldPassword }
        ],
        newPassword: [
          { required: true, trigger: 'blur', validator: validateNewPassword }
        ],
        reNewPassword: [
          { required: true, trigger: 'blur', validator: validateReNewPassword }
        ],
      }
    }
  },
  methods: {
    handleCommits: function() {
      let check = null;
      var mine = this;
      var fd = new FormData();
      if (this.commitForm.newPassword != this.commitForm.reNewPassword) {
        this.$message.error('两次输入的新密码不一致');
        return;
      }
      this.$refs.commitForm.validate(vaild => { //校验
        if (vaild) {
          console.log('表单校验结果合格');
        } else {
          console.log('表单校验参数有误');
        }
        check = vaild;
      });
      console.log(check);
      if (!check) {
        this.$message.error('您还有项目尚未输入');
        return;
      }
      console.log('表单校验通过');

      fd.append('oldPassword', this.commitForm.oldPassword);
      fd.append('newPassword', this.commitForm.newPassword);

      this.$axios({
          method: 'post',
          url: '/api/TAccountController/alterPasswordAction',
          data: fd,
          headers: {
            'token': sessionStorage.getItem('token'),
          },
        })
        .then(function(response) {
          console.log(response);
          if (response.data.status == 200) {
            if (response.data.data != null) {
              mine.$notify({
                title: '成功',
                message: '您已成功修改密码',
                type: 'success'
              })
              mine.reload();
            }
          } else {
            mine.$alert(response.data.message, "提示", {
              confirmButtonText: "确定"
            });
          }
        })
        .catch(function(error) {
          console.info(error);
        });
    }
  },
  mounted: function() {
    console.log(this);
  },
  computed: { //computed 不能计算data() 中的属性

  }
}

</script>
<style scoped>
.title_div {
  text-align: center;
}

.el-input {
  width: 450px;
}

form {
  transform: translate(33%, 0px);
}

button {
  transform: translate(258%, 10px);
}

</style>
