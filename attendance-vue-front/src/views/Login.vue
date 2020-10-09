<template>
  <el-container>
    <el-main>
      <div>
        <user-header :viewName="address.viewName" :url="address.url"></user-header>
        <!--  -->
        <el-form :model="ruleForm2" :rules="rules2" status-icon ref="ruleForm2" label-position="left" label-width="0px" class="demo-ruleFrom login-page">
          <div class="title_h4">
            <h3 class="title">系统登录</h3>
          </div>
          <el-form-item prop="phone">
            <el-input type="text" v-model="ruleForm2.phone" auto-comlete="off" placeholder="手机号">
              <template slot="prepend"><span class="fa fa-user fa-lg" style="width: 13px"></span></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input type="password" v-model="ruleForm2.password" auto-comlete="off" placeholder="密码">
              <template slot="prepend">
                <span class="fa fa-lock fa-lg" style="width: 13px"></span>
              </template>
              <template slot="suffix">
                <div class="pwd_eye_div" v-show="pwdEyeDivShow">
                  <span class="password-eye" @click="showPassword" :class="eyeType" style=""></span>
                </div>
              </template>
            </el-input>
          </el-form-item>
          <el-checkbox v-model="rememberme" class="rememberme">记住密码</el-checkbox>
          <el-form-item style="width:100%">
            <el-button type="primary" style="width:100%" :disabled="isNotValidBrowser" @click="handleSubmit" :loading="logining">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-main>
  </el-container>
</template>
<script>
import UserHeader from '@/views/account/UserHeader'
export default {
  name: "Login",
  components: {
    "user-header": UserHeader
  },
  data() {
    return {
      address: {
        viewName: "立即注册",
        url: "/regist",
      },
      logining: false,
      ruleForm2: {
        phone: '',
        password: ""
      },
      rules2: {
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" }
        ],
        password: [{
          required: true,
          message: "请输入密码",
          trigger: "blur"
        }]
      },
      isNotValidBrowser: false,
      pwdType: 'password',
      eyeType: 'fa fa-eye-slash fa-lg',
      rememberme: false,
      pwdEyeDivShow: false
    }
  },
  methods: {
    /**
     * 清空cookie
     * @return 
     */
    deleteCookie() {
      this.setCookie('', '', -1);
    },
    /**
     * 成功登陆后把用户工号和密码存入cookie，再次进入页面时读取cookie
     */
    setCookie(empNo, pwd, days) {
      let expire = new Date();
      expire.setDate(expire.getDate() + days);
      document.cookie = `acc_phone=${empNo};expires=${expire}`;
      document.cookie = `acc_pwd=${pwd};expires=${expire}`;
    },
    getCookie() {
      if (document.cookie.length) {
        let arr = document.cookie.split('; ');
        for (let i = 0; i < arr.length; i++) {
          let arr1 = arr[i].split('=');

          if (arr1[0] === 'acc_phone') {
            this.ruleForm2.phone = arr1[1];

          } else if (arr1[0] === 'acc_pwd') {
            this.ruleForm2.password = arr1[1];
            this.rememberme = true;
          }
        }
      }
    },
    /**
     * 给"眼睛"增加click事件
     */
    showPassword() {
      if (this.pwdType === 'password') {
        this.pwdType = '',
          this.eyeType = 'fa fa-eye fa-lg'
      } else {
        this.pwdType = '',
          this.eyeType = 'fa fa-eye-slash fa-lg'
      }
    },
    handleSubmit() {
      var mine = this;
      var fd = new FormData();

      fd.append('phone', this.ruleForm2.phone);
      fd.append('password', this.ruleForm2.password)
      console.log(fd);

      this.$refs.ruleForm2.validate(valid => {
        if (valid) {
          this.logining = true;
          this.$axios({
            method: 'post',
            url: '/api/TAccountController/loginAction2',
            data: fd
          }).then(function(response) {
            console.log(response);
            if (response.data.status == 200) {
              if (response.data.data != null) {
                mine.logining = false;
                sessionStorage.setItem("token", response.data.data.tokenStr);
                // sessionStorage.setItem("accId", response.data.data.id);
                sessionStorage.setItem("accPhone", response.data.data.phone);
                sessionStorage.setItem("accId", response.data.data.id);
                sessionStorage.setItem("accEmployeeNum", response.data.data.employeeNum);
                sessionStorage.setItem("accEmployeeName", response.data.data.employeeName);
                sessionStorage.setItem("accDepartment", response.data.data.department);
                sessionStorage.setItem("accRegTime", response.data.data.regTime);
                sessionStorage.setItem("accBornTime", response.data.data.bornTime);
                sessionStorage.setItem("accRole", response.data.data.role);
                sessionStorage.setItem("accMailbox", response.data.data.mailbox);
                sessionStorage.setItem("accRegion", response.data.data.region);
                sessionStorage.setItem("accPortrait", response.data.data.portrait);
                if (response.data.data.role == 0) {
                  mine.$router.push({ path: "/admin" });
                } else {
                  mine.$router.push({ path: '/NormalUser' })
                }
              }
            } else {
              console.log(mine.pwdEyeDivShow);
              mine.pwdEyeDivShow = false;
              mine.logining = false;
              mine.$alert(response.data.message, "提示", {
                confirmButtonText: "确定"
              });
            }
          }).catch(function(error) {
            console.log(error);
          })
        } else {
          console.log("提交有误!");
          return false;
        }
      });
    }
  },
  //页面载入后读取cookie
  mounted() {
    console.log(this);
  }
};

</script>
<style scoped>
.pwd_eye_div {
  transform: translateY(50%);
}

.el-main {
  padding: 0;
}

.login-page {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  margin: 120px auto;
  width: 350px;
  padding: 35px 35px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;

  .password-eye {
    position: absolute;
    right: 20px;
    top: 11px;
    cursor: pointer;
  }
}

label.el-checkbox.rememberme {
  margin: 0px 0px 15px;
  text-align: left;
}

.title_h4 {
  text-align: center;
}

</style>
