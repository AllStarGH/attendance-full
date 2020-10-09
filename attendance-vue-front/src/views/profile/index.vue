<template>
  <el-container>
    <!-- 以上为顶栏 -->
    <div class="div00">
      <el-main>
        <div class="div0h40elForm">
          <h4>我的资料</h4>
        </div>
        <el-form :model="account" ref="account" label-width="100px" class="demo-ruleForm" id="form_acc00" status-icon>
          <!--  -->
          <el-form-item label="工号" prop="accEmployeeNum">
            <el-input type="number" v-model="account.accEmployeeNum" autocomplete="off" readonly></el-input>
          </el-form-item>
          <!--  -->
          <el-form-item label="手机号码" prop="accPhone">
            <el-input type="number" v-model="account.accPhone" autocomplete="off" readonly></el-input>
          </el-form-item>
          <!--  -->
          <el-form-item label="姓名" prop="accEmployeeName">
            <!-- readOnly=true -->
            <el-input type="text" v-model="account.accEmployeeName" autocomplete="off" readonly></el-input>
          </el-form-item>
          <!--  -->
          <el-form-item label="出生日期" prop="accBornTime">
            <!-- readOnly=true -->
            <el-input type="datetime" v-model="account.accBornTime" autocomplete="off" :value="formatBornTime()" readonly></el-input>
          </el-form-item>
          <!--  -->
          <el-form-item label="邮箱" prop="accMailbox">
            <el-input type="email" v-model="account.accMailbox" autocomplete="off" readonly></el-input>
          </el-form-item>
          <!--  -->
          <el-form-item label="账号角色" prop="accMailbox">
            <el-input type="text" v-model="account.accRole==='0'?'管理员':'普通用户'" autocomplete="off" readonly></el-input>
          </el-form-item>
          <el-form-item label="所属地区" prop="accRegion">
            <el-input type="text" v-model="account.accRegion" autocomplete="off" v-bind="judgeRegion()" readonly></el-input>
          </el-form-item>
          <el-form-item label="隶属部门" prop="accDepartment">
            <el-input type="text" v-model="account.accDepartment" autocomplete="off" v-bind="judgeDept()" readonly></el-input>
          </el-form-item>
          <!--  -->
          <el-form-item label="注册时间" prop="accRegTime">
            <el-input type="datetime" v-model="account.accRegTime" autocomplete="off" :value="formatRegTime()" readonly></el-input>
          </el-form-item>
          <!--  -->
        </el-form>
      </el-main>
    </div>
  </el-container>
</template>
<script type="text/javascript">
export default {
  name: 'Profile',
  data() {
    var account = {
      accEmployeeName: sessionStorage.getItem('accEmployeeName'),
      accPhone: sessionStorage.getItem('accPhone'),
      accEmployeeNum: sessionStorage.getItem('accEmployeeNum'),
      accPortrait: sessionStorage.getItem('accPortrait'),
      accMailbox: sessionStorage.getItem('accMailbox'),
      accRole: sessionStorage.getItem('accRole'),
      accBornTime: '',
      accDepartment: '',
      accRegion: '',
      accRegTime: '',
    };
    return {
      account,
    }
  },
  methods: { //computed是属性调用，而methods是函数调用
    formatRegTime: function() {
      let regTime = sessionStorage.getItem('accRegTime');
      var date = new Date(regTime);
      // 年月日
      var gener = date.getFullYear();
      gener += '-';
      gener += (date.getMonth() + 1);
      gener += '-';
      gener += date.getDate();
      // 时分秒
      gener += ' ';
      gener += date.getHours() + ':';
      gener += date.getMinutes() + ':';
      gener += date.getSeconds();
      this.account.accRegTime = gener;
    },
    formatBornTime: function() {
      let born = sessionStorage.getItem('accBornTime');
      var yMd = born.split('T')
      this.account.accBornTime = yMd[0]
    },
    judgeRegion: function() {
      let key = sessionStorage.getItem('accRegion')
      let area = null;
      switch (key) {
        case '0':
          area = "安淮";
          break;
        case '1':
          area = "南徽";
          break;
        case '2':
          area = "古丰";
          break;
        case '3':
          area = "方千";
          break;
        case '4':
          area = "播星";
          break;
        case '5':
          area = "角港";
          break;
        case '6':
          area = "米园";
          break;
        case '7':
          area = "湖地";
          break;
        case '8':
          area = "寿韫";
          break;
        case '9':
          area = "天玺台";
          break;
        case '10':
          area = "苏崧";
          break;
        case '11':
          area = "南威";
          break;
        case '12':
          area = "都张集";
          break;
        case '13':
          area = "企塘";
          break;
        case '14':
          area = "中缇";
          break;
      }
      this.account.accRegion = area;
    },
    judgeDept: function() {
      let key = sessionStorage.getItem('accDepartment');
      let position = null;
      switch (key) {
        case '0':
          position = "研发";
          break;
        case '1':
          position = "财务";
          break;
        case '2':
          position = "人事";
          break;
        case '3':
          position = "销售";
          break;
        case '4':
          position = "公关";
          break;
        case '5':
          position = "法务";
          break;
        case '6':
          position = "安全";
          break;
      }
      this.account.accDepartment = position;
    },
    jumpTo(uri) {
      this.$router.push(uri)
    }
  },
  computed: { //computed 不能计算data() 中的属性
  },
  watch: {

  },
  mounted() {
    console.log(this)
  }
}

</script>
<style>
#form_acc00 {
  width: 30%;
  transform: translate(110%, -25px);
}

.div00 {
  width: 100%;
}

.div0h40elForm {
  width: 100%;
  text-align: center;
  transform: translate(0px, -20px)
}

</style>
