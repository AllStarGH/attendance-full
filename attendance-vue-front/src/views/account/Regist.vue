<template>
  <el-container>
    <user-header :viewName="address.viewName" :url="address.url"></user-header>
    <!--  -->
    <el-main>
      <el-row class="row_bg" justify="center" type="flex">
        <div class="reg_form_div">
          <div class="title_facade">
            <h4>{{title}}</h4>
          </div>
          <!--  -->
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <!-- 名字 -->
            <el-form-item label="请输入名字" prop="employeeName" class="mine_input_item">
              <el-col :span="spanWidth">
                <el-input v-model="ruleForm.employeeName" maxlength="30"></el-input>
              </el-col>
            </el-form-item>
            <!-- 年龄 -->
            <el-form-item label="请选择年龄" required class="mine_input_item">
              <el-col :span="spanWidth">
                <el-form-item prop="bornTime">
                  <el-date-picker type="date" placeholder="选择年月日" v-model="ruleForm.bornTime" style="width: 100%;"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-form-item>
            <!-- 手机号 -->
            <el-form-item label="请输入手机号" prop="phone" class="mine_input_item">
              <el-col :span="spanWidth">
                <el-input v-model="ruleForm.phone" maxlength="30"></el-input>
              </el-col>
            </el-form-item>
            <!-- 邮箱 -->
            <el-form-item label="请输入邮箱地址" prop="mailbox" class="mine_input_item">
              <el-col :span="spanWidth">
                <el-input v-model="ruleForm.mailbox" maxlength="30"></el-input>
              </el-col>
            </el-form-item>
            <!-- 邮箱验证码 -->
            <el-form-item label="请输入邮箱收到的验证码" class="mine_input_item">
              <el-col :span="spanWidth">
                <el-form-item prop="veriCode">
                  <el-input v-model="ruleForm.veriCode" maxlength="6"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="2">
                <el-button @click="sendCodeToMail" id="mine_code_btn" size="medium" type="primary" round>发送验证码至邮箱</el-button>
              </el-col>
            </el-form-item>
            <!-- 地区 -->
            <el-form-item label="新地区" prop="region" required>
              <el-select v-model="ruleForm.region" placeholder="请选择所在地区" class="form_inp">
                <el-option v-for="item in regionOpts" :key="item.value" :value="item.value" :label="item.label">
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 部门 -->
            <el-form-item label="新部门" prop="department" required>
              <el-select v-model="ruleForm.department" placeholder="请选择隶属部门">
                <el-option v-for="item in departmentOpts" :key="item.value" :value="item.value" :label="item.label">
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 权限角色 -->
            <el-form-item prop="role" required>
              <el-radio v-model="ruleForm.role" :label="0" border>管理员 </el-radio>
              <el-radio v-model="ruleForm.role" :label="1" border>普通雇员</el-radio>
            </el-form-item>
            <!-- 密码 -->
            <el-form-item label="请输入密码" prop="password" class="mine_input_item">
              <el-col :span="spanWidth">
                <el-input v-model="ruleForm.password" show-password maxlength="16"></el-input>
              </el-col>
            </el-form-item>
            <!-- 确认密码 -->
            <el-form-item label="请再次输入密码" prop="rePassword" class="mine_input_item">
              <el-col :span="spanWidth">
                <el-input v-model="ruleForm.rePassword" show-password maxlength="16"></el-input>
              </el-col>
            </el-form-item>
            <!-- button -->
            <el-form-item>
              <el-button type="primay" id="mine_submit_btn" @click="submitForm('ruleForm')">提交</el-button>
              <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-row>
    </el-main>
    <el-footer>
      <div><span>Powered by ® 2019</span></div>
    </el-footer>
  </el-container>
</template>
<script>
import UserHeader from '@/views/account/UserHeader'
export default {
  name: 'Regist',
  components: {
    "user-header": UserHeader
  },
  data() {
    const validateMailbox = (rule, value, callback) => {
      var mailExp = /\w+([-+.']\w+)*@\w+([-.]w+)*\.\w+([-.]\w+)*/;
      let exp = new RegExp(mailExp);

      if (exp.test(value) == false) {
        callback(new Error('请输入正确的邮箱地址'));
      } else {
        callback();
      }
    };
    const validatePhone = (rule, value, callback) => {
      //例如:13774157471
      var phoneExp = /^(13[0-9]|14[0-9]|15[0-9]|166|17[0-9]|18[0-9]|19[8|9])\d{8}$/;
      let exp = new RegExp(phoneExp);

      if (exp.test(value) == false) {
        callback(new Error('请输入正确的手机号码'));
      } else {
        callback();
      }
    };
    return { //Data initialize
      address: {
        viewName: "马上登录",
        url: "/login",
      },
      spanWidth: 12,
      title: "注册新用户",
      ruleForm: {
        rePassword: "123",
        veriCode: "",
        employeeName: "",
        role: null,
        password: "123",
        mailbox: "",
        phone: "13774157479",
        department: null,
        region: null,
        bornTime: null,
      },
      rules: {
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur' }
        ],
        rePassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入正确的手机号码', trigger: 'blur', validator: validatePhone },
        ],
        mailbox: [
          { required: true, message: '请输入正确的邮箱地址', trigger: 'blur', validator: validateMailbox },
        ],
        bornTime: [
          { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
        ],
        employeeName: [
          { required: true, message: '请输入名字', trigger: 'blur' },
          { min: 3, max: 30, message: '长度在 3 到 30 个字符', trigger: 'blur' }
        ],
        region: [
          { required: true, message: '请选择区域', trigger: 'change' }
        ],
        department: [
          { required: true, message: '请选择区域', trigger: 'change' }
        ],
        role: [
          { required: true, message: '请选择权限角色', trigger: 'change' }
        ],
      },
      regionOpts: [
        { value: 0, label: "安淮" },
        { value: 1, label: "南徽" },
        { value: 2, label: "古丰" },
        { value: 3, label: "方千" },
        { value: 4, label: "播星" },
        { value: 5, label: "角港" },
        { value: 6, label: "米园" },
        { value: 7, label: "湖地" },
        { value: 8, label: "寿韫" },
        { value: 9, label: "天玺台" },
        { value: 10, label: "苏崧" },
        { value: 11, label: "南威" },
        { value: 12, label: "都张集" },
        { value: 13, label: "企塘" },
        { value: 14, label: "中缇" },
      ],
      departmentOpts: [
        { value: 0, label: "研发" },
        { value: 1, label: "财务" },
        { value: 2, label: "人事" },
        { value: 3, label: "销售" },
        { value: 4, label: "公关" },
        { value: 5, label: "法务" },
        { value: 6, label: "安全" },
      ],
    }
  },
  methods: {
    sendCodeToMail() {
      var mine = this;
      // regular expression
      var mailExp = /\w+([-+.']\w+)*@\w+([-.]w+)*\.\w+([-.]\w+)*/;
      var mailboxExp = new RegExp(mailExp);

      let box = this.ruleForm.mailbox;
      if (box == null || box == '') {
        this.$notify({
          title: '错误',
          message: '邮箱为空',
          type: 'error'
        });
        throw new Error('邮箱为空');
      }
      if (mailboxExp.test(this.ruleForm.mailbox) == false) {
        let errMsg = '请输入正确的邮箱地址';
        this.$notify({
          title: '错误',
          message: errMsg,
          type: 'error'
        });
        throw new Error(errMsg);
      }
      console.info("parameter have through...");

      this.$axios({
          method: 'post',
          responseType: 'json',
          url: '/api/TAccountController/sendCaptchaCodeAction',
          data: mine.ruleForm.mailbox,
        })
        .then(function(response) {
          console.info(response);
          if (response.data.status === 200) {
            if (response.data.data != null) {
              mine.$message({
                message: '验证码发送至邮箱成功,有效期10分钟',
                type: 'success'
              });
            }
          } else {
            mine.$notify({
              title: '错误',
              message: response.data.message,
              duration: 0
            });
          }
        })
        .catch(function(error) {
          console.error(error);
        })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    submitForm(formName) {
      let flag = true;
      var mine = this;
      var account = mine.ruleForm;

      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.info('验证通过,submit!,vaild=>' + valid);
        } else {
          console.log('error submit=>' + valid);
          flag = valid;
        }
      });
      if (!flag) {
        return;
      }
      if (this.ruleForm.password != this.ruleForm.rePassword) {
        this.$notify({
          title: '密码错误',
          message: "两次输入的密码不一致",
        });
      }
      console.log(this.ruleForm);

      this.$axios({
          method: 'post',
          url: '/api/TAccountController/regAction1',
          data: account,
          // `transformRequest` 允许在向服务器发送前，修改请求数据
          transformRequest: [
            function(data) {
              return JSON.stringify(data);
            }
          ],
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          },
        })
        .then(function(response) {
          console.log(response);
          if (response.data.status === 200) {
            if (response.data.data != null) {
              mine.$router.push('/login');
            }
          } else {
            mine.$alert(response.data.message, '错误', { confirmButtonText: '确定' });
          }
        })
        .catch(function(error) {
          console.log(error);
        });
    },
  },
  beforeCreate() {
    console.log("创建前:");
    console.log(this.$el);
    console.log(this.$data);
  },
  created() {
    console.log("创建完成:");
    console.log(this.$el);
    console.log(this.$data);
  },
  beforeMount() {
    console.log("挂载前:");
    console.log(this.$el);
    console.log(this.$data);
  },
  mounted() {
    console.log("挂载完成:");
    console.log(this.$el);
    console.log(this.$data);
    console.log(this);
  },
  beforeUpdate() {
    console.log('即将更新渲染');
  },
  updated() {
    console.log('更新成功');
  },
  beforeDestory() {
    console.log("销毁前:");
  },
  destoryed() {
    console.log("销毁完成:");
  }
}

</script>
<style>
.title_facade {
  text-align: center;
}

#mine_code_btn {
  transform: translateX(25px);
}

body {
  overflow: auto;
}

.mine_input_item {
  width: 155%;
}

#mine_submit_btn {
  color: rgb(255, 255, 255);
  border-color: rgb(64, 158, 255);
  background-color: rgb(64, 158, 255);
}

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

</style>
