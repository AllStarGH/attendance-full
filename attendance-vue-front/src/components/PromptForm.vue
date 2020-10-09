<template>
  <el-dialog :visible.sync="openDialog" :title="title">
    <div class="form_div_emitts">
      <el-form :model="rulePropmtForm" :rules="rules" ref="rulePropmtForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="新名字" prop="employeeName">
          <el-input v-model="rulePropmtForm.employeeName" maxlength="30">
          </el-input>
        </el-form-item>
        <el-form-item label="新手机号" prop="phone">
          <el-input v-model="rulePropmtForm.phone" maxlength="26">
          </el-input>
        </el-form-item>
        <el-form-item label="新邮箱" prop="mailbox">
          <el-input v-model="rulePropmtForm.mailbox" maxlength="40">
          </el-input>
        </el-form-item>
        <el-form-item label="新地区" prop="region">
          <el-select v-model="rulePropmtForm.region" placeholder="请选择所在地区" class="form_inp">
            <el-option v-for="item in regionOpts" :key="item.value" :value="item.value" :label="item.label">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="新部门" prop="department">
          <el-select v-model="rulePropmtForm.department" placeholder="请选择隶属部门" class="form_inp">
            <el-option v-for="item in departmentOpts" :key="item.value" :value="item.value" :label="item.label">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="新年龄" prop="bornTime">
          <el-col :span="11">
            <el-form-item prop="bornTime">
              <el-date-picker type="date" placeholder="选择日期" v-model="rulePropmtForm.bornTime" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item prop="role">
          <el-radio v-model="rulePropmtForm.role" :label="0" border>管理员 </el-radio>
          <el-radio v-model="rulePropmtForm.role" :label="1" border>普通雇员</el-radio>
        </el-form-item>
        <el-form-item>
          <el-button type="primay" @click.native.prevent="commitFormData">提交</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>
<script>
export default {
  inject: ['reload'],
  name: "PromptForm",
  computed: {
    openDialog: {
      get() {
        return this.visible;
      },
      set(val) {
        console.log(val);
        this.$emit("update:visible", val); //openDialog 改变的时候通知父组件
      }
    }
  },
  data() {
    const validateMailbox = (rule, value, callback) => {
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
    const validatePhone = (rule, value, callback) => {
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
    const validateEmployeeName = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error('请输入新的用户名'))
      } else {
        callback()
      }
    };
    const validateBornTime = (rule, value, callback) => {
      if (value == null) {
        callback(new Error('请选择新的出生日期'));
      } else {
        callback();
      }
    };
    const validateRegion = (rule, value, callback) => {
      if (value == null) {
        callback(new Error('请选择新的权限角色'));
      } else {
        callback();
      }
    };
    const validateDepartment = (rule, value, callback) => {
      if (value == null) {
        callback(new Error('请选择新的在职部门'));
      } else {
        callback();
      }
    };
    return {
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
      title: "编辑员工资料",
      message: 'Vue的生命周期',
      rules: {
        phone: [
          { required: true, trigger: 'blur', validator: validatePhone }
        ],
        mailbox: [
          { required: true, trigger: 'blur', validator: validateMailbox }
        ],
        employeeName: [
          { required: true, trigger: 'blur', validator: validateEmployeeName }
        ],
        bornTime: [
          { required: true, trigger: 'blur', validator: validateBornTime }
        ],
        region: [
          { required: true, trigger: 'blur', validator: validateRegion }
        ],
        department: [
          { required: true, trigger: 'blur', validator: validateDepartment }
        ],
      },
    }
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    rulePropmtForm: {
      role: null,
      phone: "",
      region: null,
      mailbox: "",
      department: null,
      bornTime: null,
      employeeName: "",
    },
  },
  model: {},
  methods: {
    commitFormData() { //提交修改后的资料
      var mine = this;

      this.$refs.rulePropmtForm.validate(vaild => {
        if (vaild) {
          console.log("vaild=" + vaild);
        } else {
          console.log("vaild=" + vaild);
          return false;
        }
      });
      console.log(this.rulePropmtForm);

      this.$axios({
          method: 'POST',
          url: '/api/TAccountController/editProfileByAdminAction',
          data: this.rulePropmtForm,
          transRequest: [
            function(data) {
              return JSON.stringfy(data);
            }
          ],
          headers: {
            'tokenStr': sessionStorage.getItem('token'),
          }
        })
        .then(function(response) {
          console.info(response)
          if (response.data.status === 200) {
            if (response.data.data != null) {
              mine.$notify({
                title: '修改成功',
                message: '您已成功修改了' + response.data.data + '名员工的资料',
                type: 'success'
              });
              // mine.reload(); //重新加载
              mine.visible = false;
            }
          } else {
            mine.$alert(response.data.message, '错误提示', { confirmButtonText: '确定' })
          }
        })
        .catch(function(error) {
          console.warn(error)
        })
    },
  },
  beforeCreate: function() {
    // console.group('------beforeCreate创建前状态------');
    console.log('------beforeCreate创建前状态------');
    console.log("%c%s", "color:red", "el     : " + this.$el); //undefined
    console.log("%c%s", "color:red", "data   : " + this.$data); //undefined 
    console.log("%c%s", "color:red", "message: " + this.message)
  },
  created: function() {
    console.info('------created创建完毕状态------');
    console.log("%c%s", "color:red", "el     : " + this.$el); //undefined
    console.log("%c%s", "color:red", "data   : " + this.$data); //已被初始化 
    console.log("%c%s", "color:red", "message: " + this.message); //已被初始化
  },
  beforeMount: function() {
    console.info('------beforeMount挂载前状态------');
    console.log("%c%s", "color:red", "el     : " + (this.$el)); //已被初始化
    console.log(this.$el);
    console.log("%c%s", "color:red", "data   : " + this.$data); //已被初始化  
    console.log("%c%s", "color:red", "message: " + this.message); //已被初始化  
  },
  mounted: function() {
    console.info('------mounted 挂载结束状态------');
    console.log("%c%s", "color:red", "el     : " + this.$el); //已被初始化
    console.log(this.$el);
    console.log("%c%s", "color:red", "data   : " + this.$data); //已被初始化
    console.log("%c%s", "color:red", "message: " + this.message); //已被初始化 
    console.info(this);
  },
  beforeUpdate: function() {
    console.info('beforeUpdate 更新前状态===============>>');
    console.log("%c%s", "color:red", "el     : " + this.$el);
    console.log(this.$el);
    console.log("%c%s", "color:red", "data   : " + this.$data);
    console.log("%c%s", "color:red", "message: " + this.message);
  },
  updated: function() {
    console.info('updated 更新完成状态===============>>');
    console.log("%c%s", "color:red", "el     : " + this.$el);
    console.log(this.$el);
    console.log("%c%s", "color:red", "data   : " + this.$data);
    console.log("%c%s", "color:red", "message: " + this.message);
  },
  beforeDestroy: function() {
    console.info('beforeDestroy 销毁前状态===============>>');
    console.log("%c%s", "color:red", "el     : " + this.$el);
    console.log(this.$el);
    console.log("%c%s", "color:red", "data   : " + this.$data);
    console.log("%c%s", "color:red", "message: " + this.message);
  },
  destroyed: function() {
    console.info('destroyed 销毁完成状态===============>>');
    console.log("%c%s", "color:red", "el     : " + this.$el);
    console.log(this.$el);
    console.log("%c%s", "color:red", "data   : " + this.$data);
    console.log("%c%s", "color:red", "message: " + this.message)
  }
}

</script>
<style scoped>
</style>
