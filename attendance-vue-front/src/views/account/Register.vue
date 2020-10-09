<template>
  <div>
    <!-- useless -->
    <div class="title_facade" id="_facade">
      <h1>注册</h1>
    </div>
    <!--  -->
    <div>
      <div id="ajax-hook"></div>
      <div class="wrap">
        <div class="wpn">
          <div class="form-data pos">
            <a href=""><img src="@/assets/UIlogo.jpg" class="head-logo"></a>
            <form class="reg_form">
              <!-- Form -->
              <p class="p-input pos">
                <label for="username" class="lab_tip">姓名</label>
                <input v-model="inputsForm.employeeName" type="text" autocomplete="off" class="form_inp _inp" placeholder="请输入姓名" maxlength="18">
                <span class="tel-warn tel-err hide"><em></em><i class="icon-warn"></i></span>
              </p>
              <p class="p-input pos">
                <input type="date" v-model="inputsForm.bornTime" autocomplete="off" class="form_inp" placeholder="请选择日期">
                <span class="tel-warn tel-err hide"><em></em><i class="icon-warn"></i></span>
              </p>
              <!--  -->
              <p class="p-input pos">
                <label for="tel" class="lab_tip">手机</label>
                <input type="text" v-model="inputsForm.phone" id="tel" autocomplete="off" class="form_inp" placeholder="请输入手机号" maxlength="48">
                <span class="tel-warn tel-err hide"><em></em><i class="icon-warn"></i></span>
              </p>
              <p class="p-input pos">
                <input type="text" v-model="inputsForm.mailbox" class="form_inp" placeholder="请输入邮箱地址" maxlength="99">
              </p>
              <p class="p-input pos" id="sendcode">
                <label for="veri-code" class="lab_tip">验证码</label>
                <input type="number" id="veri-code" v-model="inputsForm.veriCode" class="form_inp" placeholder="请输入邮箱验证码" @input="limitVeriLen">
                <a @click="sendCodeToMail" class="send" id="send_veri">发送验证码至邮箱</a>
                <span class="time hide"><em>120</em>s</span>
                <span class="error hide"><em></em><i class="icon-warn" style="margin-left: 5px"></i></span>
              </p>
              <p class="p-input pos" id="pwd">
                <label for="passport" class="lab_tip">密码</label>
                <input v-model="inputsForm.password" type="password" id="passport" class="form_inp" placeholder="请输入密码" maxlength="55">
                <span class="tel-warn pwd-err hide"><em></em><i class="icon-warn" style="margin-left: 5px"></i></span>
              </p>
              <p class="p-input pos" id="confirmpwd">
                <label for="passport2" class="lab_tip">确认密码</label>
                <input type="password" maxlength="55" id="passport2" class="form_inp" placeholder="请确认密码" v-model="rePassword">
                <span class="tel-warn confirmpwd-err hide"><em></em><i class="icon-warn" style="margin-left: 5px"></i></span>
              </p>
              <div>
                <p class="p-input pos">
                  <select v-model="inputsForm.region" class="form_inp selects">
                    <option value="-1"> -- 请选择所在地区 -- </option>
                    <option value="0">安淮</option>
                    <option value="1">南徽</option>
                    <option value="2">古丰</option>
                    <option value="3">方千</option>
                    <option value="4">播星</option>
                    <option value="5">角港</option>
                    <option value="6">米园</option>
                    <option value="7">湖地</option>
                    <option value="8">寿韫</option>
                    <option value="9">天玺台</option>
                    <option value="10">苏崧</option>
                    <option value="11">南威</option>
                    <option value="12">都张集</option>
                    <option value="13">企塘</option>
                    <option value="14">中缇</option>
                  </select>
                </p>
                <p class="p-input pos">
                  <select v-model="inputsForm.department" class="form_inp selects">
                    <option value="-1"> -- 请选择所属部门 -- </option>
                    <option value="0">研发</option>
                    <option value="1">财务</option>
                    <option value="2">人事</option>
                    <option value="3">销售</option>
                    <option value="4">公关</option>
                    <option value="5">法务</option>
                    <option value="6">安全</option>
                  </select>
                </p>
              </div>
              <div class="tag">
                <h3 class="tag_h3">请选择账号角色</h3>
                <div class="role_for_title">
                  <span>
                    <div>
                      <em>管理人员</em>
                      <em>普通用户</em>
                    </div>
                  </span>
                </div>
                <div class="button-holder">
                  <input type="radio" id="radio-1-1" v-model="inputsForm.role" class="regular-radio" value="0">
                  <label for="radio-1-1" class="tag-label"></label>
                  <input type="radio" id="radio-1-2" v-model="inputsForm.role" class="regular-radio" value="1" checked>
                  <label for="radio-1-2" class="tag-label"></label>
                </div>
              </div>
            </form>
            <button @click="register()" class="lang-btn">注册</button>
            <div class="bottom-info">已有账号，<a href="javascript:void(0);" @click="skipLogin()">马上登录</a></div>
            <div class="third-party">
              <div>
                <p class="address_p">
                  <a href="/attendance/" title="返回首页">返回首页</a>
                </p>
              </div>
            </div>
            <p class="right">Powered by ® 2019</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'Register',
  data() {
    return { //Data initialize
      rePassword: '',
      inputsForm: {
        veriCode: '',
        employeeName: '',
        role: 1,
        password: '',
        mailbox: '',
        phone: '',
        department: -1,
        region: -1,
        bornTime: null
      }
    }
  },
  methods: {
    sendCodeToMail() {
      var mine = this;
      //mailbox of exp
      var mailExp = /\w+([-+.']\w+)*@\w+([-.]w+)*\.\w+([-.]\w+)*/;
      var mailboxExp = new RegExp(mailExp);

      let box = this.$data.inputsForm.mailbox;
      console.log('box=' + box);
      if (box == null || box == '') {
        this.$notify({
          title: '错误',
          message: '邮箱为空',
          type: 'error'
        });
        throw new Error('邮箱为空');
      }
      if (mailboxExp.test(this.inputsForm.mailbox) == false) {
        let errMsg = '请输入正确的邮箱地址';
        this.$notify({
          title: '错误',
          message: errMsg,
          type: 'error'
        });
        throw new Error(errMsg);
      }
      console.info('参数通行');

      this.$axios({
        method: 'post',
        url: '/api/TAccountController/sendCaptchaCodeAction',
        data: mine.inputsForm.mailbox,
      }).then(function(response) {
        console.info(response);
        if (response.data.status === 200) {
          mine.$message({
            message: '验证码发送至邮箱成功,有效期10分钟',
            type: 'success'
          });
        } else {
          mine.$notify({
            title: '错误',
            message: response.data.message,
            duration: 0
          });
        }
      }).catch(function(error) {
        console.error(error);
      })
    },
    limitVeriLen() { //当输入超过n位后，会进行截断，保留前面的n位
      if (this.inputsForm.veriCode.length > 8)
        this.inputsForm.veriCode = this.inputsForm.veriCode.slice(0, 8)
    },
    skipLogin() {
      this.$router.push("/attendance/login");
    },
    register() {
      //常规提交参数校验
      let flag1 = this.validateForm();
      console.log(flag1);
      if (flag1 == false) {
        return;
      }

      // 正则表达校验
      let flag2 = this.validateFormRegExp();
      console.log(flag2);
      if (flag2 == false) {
        return;
      }

      var mine = this;
      var account = mine.inputsForm;
      this.$axios({
        method: 'post',
        url: '/api/TAccountController/regAction',
        // `transformRequest` 允许在向服务器发送前，修改请求数据
        transformRequest: [
          function(data) {
            return mine.$qs.stringify(data); // 使用qs来解决跨域问题
          }
        ],
        data: account,
      }).then(function(response) {
        console.log(response);
        if (response.data.status === 200) {
          mine.$router.push('/login');
        } else {
          mine.$alert(response.data.message, '错误', { confirmButtonText: '确定' });
        }
      }).catch(function(error) {
        console.log(error);
      });
    },
    validateFormRegExp() {
      let flag = true;
      let errMsg = null;
      //mailbox of exp
      var mailExp = /\w+([-+.']\w+)*@\w+([-.]w+)*\.\w+([-.]\w+)*/;
      var mailboxExp = new RegExp(mailExp);
      //phone of exp
      var phoneExp = /^(13[0-9]|14[0-9]|15[0-9]|166|17[0-9]|18[0-9]|19[8|9])\d{8}$/; //例如:13774157471
      var phExp = new RegExp(phoneExp);

      try {
        if (mailboxExp.test(this.inputsForm.mailbox) == false) {
          errMsg = '请输入正确的邮箱地址'
          throw new Error(errMsg)
        }
        if (phExp.test(this.inputsForm.phone) == false) {
          errMsg = '请输入正确的手机号码'
          throw new Error(errMsg)
        }
        if (this.inputsForm.password != this.rePassword) {
          errMsg = '两次输入的密码不一致'
          throw new Error(errMsg)
        }
      } catch (err) {
        this.$notify({
          title: '格式不规范',
          message: errMsg,
          type: 'warning'
        });
        console.error(err);
        flag = false;
      }
      return flag;
    },
    validateForm() {
      let flag = true;
      let errMsg = '';
      try {
        if (this.inputsForm.employeeName == '' || this.inputsForm.employeeName == null) {
          errMsg = '姓名未输入'
          throw new Error(errMsg)
        }
        if (this.inputsForm.password == '' || this.inputsForm.password == null) {
          errMsg = '密码未输入'
          throw new Error(errMsg)
        }
        if (this.rePassword == '' || this.rePassword == null) {
          errMsg = '请再输入一次确认密码'
          throw new Error(errMsg)
        }
        if (this.inputsForm.phone == '' || this.inputsForm.phone == null) {
          errMsg = '手机号码未输入'
          throw new Error(errMsg)
        }
        if (this.inputsForm.mailbox == '' || this.inputsForm.mailbox == null) {
          errMsg = '邮箱地址未输入'
          throw new Error(errMsg)
        }
        if (this.inputsForm.bornTime == '' || this.inputsForm.bornTime == null) {
          errMsg = '未选择出生日期'
          throw new Error(errMsg)
        }
        if (this.inputsForm.region == -1 || this.inputsForm.department == -1) {
          errMsg = '未选择所在地区或所属部门'
          throw new Error(errMsg)
        }
        if (this.inputsForm.veriCode == '' || this.inputsForm.veriCode == null) {
          errMsg = '未输入邮箱验证码'
          throw new Error(errMsg)
        }
      } catch (err) {
        this.$alert(errMsg, '提交失败', { confirmButtonText: '确定' });
        console.error(err);
        flag = false;
      }
      return flag;
    },
  },
  mounted() {
    console.log(this);
  }
}

</script>
<!--  -->
<style src="../../styles/base.css"></style>
<style src="../../styles/iconfont.css"></style>
<style src="../../styles/register.css"></style>
<!--  -->
<style scoped>
#send_veri {
  cursor: pointer;
}

.role_for_title em {
  margin: 0 12px;
}

.role_for_title {
  color: gray;
  font-size: 16px;
  transform: translateY(15px);
}

.tag_h3 {
  color: #898787;
}

.tag-label {
  display: inline;
  margin: 0 35px;
}

.tag {
  width: 380px;
  height: 90px;
  margin: 20px auto;
  text-align: center;
}

.regular-radio {
  display: none;
}

.regular-radio+label {
  -webkit-appearance: none;
  background-color: #fff;
  border: 1px solid #aaa;
  padding: 9px;
  border-radius: 50px;
  display: inline-block;
  position: relative;
}

.regular-radio:checked+label:after {
  content: ' ';
  width: 12px;
  height: 12px;
  border-radius: 50px;
  position: absolute;
  top: 3px;
  background: #47d9bf;
  box-shadow: 0px 0px 5px 0px #47d9bf;
  left: 3px;
}

.selects option {
  font-weight: normal;
  display: block;
  white-space: pre;
  min-height: 1.2em;
  padding: 0px 2px 1px;
  transition: transform .15s linear;
  cursor: pointer;
  user-select: none;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.selects {
  font-family: 'Source Sans Pro', Arial, sans-serif;
  width: 340px;
  height: 45px;
  border-radius: 6px;
  background-color: #ffffff;
  border: 1px solid #dfdedf;
  perspective: 1000px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
}

.lab_tip {
  display: none;
}

#born_lab {
  display: none;
}

._inp,
.form_inp {
  color: #898787;
  font-size: 15px;
}

#born_inp {
  color: #898787;
  font-size: 15px;
}

.address_p {
  font-size: 14px;
}

/* title_facade */
.title_facade {
  text-align: center;
  margin: 22px 0;
  color: #3895e8;
}

#_facade {
  display: none;
}

.wpn {
  transform: translate(0px, 110px);
}

</style>
