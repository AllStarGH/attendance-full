<template>
  <el-container>
    <!-- 修改个人基本资料 -->
    <div class="title_div">
      <h3>修改基本资料</h3>
    </div>
    <div>
      <el-form label-width="100px" ref="submitForm" :model="submitForm" :rules="submitRules" auto-complete="on" label-position="center">
        <el-form-item prop="employeeName" label="请输入新的用户名">
          <el-input v-model="submitForm.employeeName" autofocus="autofocus" type="text" auto-complete="on" @keyup.enter.navite="handleSubmits" maxlength="32" />
        </el-form-item>
        <!--  -->
        <el-form-item prop="phone" label="请输入新的手机号">
          <el-input v-model="submitForm.phone" autofocus="autofocus" type="text" auto-complete="on" @keyup.enter.navite="handleSubmits" maxlength="26" />
        </el-form-item>
        <!--  -->
        <el-form-item prop="bornTime" label="请选择新的出生日期" style="width: 550px;">
          <el-date-picker type="date" placeholder="选择年月日" v-model="submitForm.bornTime" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <!--  -->
        <el-form-item prop="mailbox" label="请输入新的邮箱地址">
          <el-input v-model="submitForm.mailbox" autofocus="autofocus" type="text" auto-complete="on" @keyup.enter.navite="handleSubmits" maxlength="40" />
        </el-form-item>
        <!--  -->
        <el-form-item class="btn_item">
          <el-button class="submit_btn" :loading="loading" @click.native.prevent="handleSubmits">
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
  name: 'EditProfile',
  data() {
    const validateAccEmployeeName = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error('请输入新的用户名'))
      } else {
        callback()
      }
    };
    const validateAccBornTime = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error('请选择新的出生日期'));
      } else {
        callback();
      }
    };
    const validateAccMailBox = (rule, value, callback) => {
      var mailExp = /\w+([-+.']\w+)*@\w+([-.]w+)*\.\w+([-.]\w+)*/;
      var mailboxExp = new RegExp(mailExp);

      if (value.length == 0) {
        callback(new Error('请输入新的邮箱地址'));
      } else if (mailboxExp.test(value) == false) {
        callback(new Error('请输入正确的邮箱地址'));
      } else {
        callback();
      }
    };
    const validateAccPhone = (rule, value, callback) => {
      //例如:13774157471
      var phoneExp = /^(13[0-9]|14[0-9]|15[0-9]|166|17[0-9]|18[0-9]|19[8|9])\d{8}$/;
      var phExp = new RegExp(phoneExp);

      if (value.length == 0) {
        callback(new Error('请输入新的手机号码'));
      } else if (phExp.test(value) == false) {
        callback(new Error('请输入正确的手机号码'));
      } else {
        callback();
      }
    };
    return {
      loading: false,
      submitForm: {
        employeeName: sessionStorage.getItem('accEmployeeName'),
        bornTime: sessionStorage.getItem('accBornTime'),
        mailbox: sessionStorage.getItem('accMailbox'),
        phone: sessionStorage.getItem('accPhone'),
      },
      submitRules: {
        phone: [
          { required: true, trigger: 'blur', validator: validateAccPhone }
        ],
        employeeName: [
          { required: true, trigger: 'blur', validator: validateAccEmployeeName }
        ],
        bornTime: [
          { require: true, trigger: 'blur', validator: validateAccBornTime }
        ],
        mailbox: [
          { require: true, trigger: 'blur', validator: validateAccMailBox }
        ],
      }
    }
  },
  methods: {
    handleSubmits: function() {
      var mine = this;
      let check = null;
      this.$refs.submitForm.validate(vaild => { //校验
        if (vaild) {
          console.log('表单校验结果合格');
        } else {
          console.log('表单校验参数有误');
        }
        check = vaild;
      });
      console.log(check);
      if (!check) {
        this.$message.error('表单校验参数有误,请检查无误后再提交');
        return;
      }
      console.log('表单校验通过');

      this.$axios({
          method: 'post',
          url: '/api/TAccountController/editProfileAction1',
          data: this.submitForm,
          transRequest: [
            function(data) {
              return JSON.stringfy(data);
            }
          ],
          headers: {
            'tokenStr': sessionStorage.getItem('token'),
          },
        })
        .then(function(response) {
          console.log(response);
          if (response.data.status == 200) {
            if (response.data.data != null) {
              mine.$notify({
                title: '成功',
                message: '您已成功修改个人资料',
                type: 'success'
              })
              console.log(response.data);
              if (response.data.data != null) {
                sessionStorage.setItem("token", response.data.data.tokenStr);
                sessionStorage.setItem("accEmployeeName", response.data.data.employeeName);
                sessionStorage.setItem("accPhone", response.data.data.phone);
                sessionStorage.setItem("accBornTime", response.data.data.bornTime);
                sessionStorage.setItem("accMailbox", response.data.data.mailbox);
                mine.reload(); //重新加载
              }
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
    console.log(this)
  }

}

</script>
<style scoped>
.title_div {
  text-align: center;
}

.submit_btn {
  transform: translate(240%, 0px);
}

.el-input {
  width: 450px;
}

form {
  transform: translate(33%, 0px);
}

</style>
