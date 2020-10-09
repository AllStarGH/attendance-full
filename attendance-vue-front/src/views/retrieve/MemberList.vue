<template>
  <el-container>
    <div class="div_mem_list">
      <div class="title_head">
        <h4>全体员工列表</h4>
      </div>
      <!--  -->
      <div class="tbl_1">
        <el-table ref="tbl_list" :data="tableData" @selection-change="handleSelectionChange" style="width:100%" border>
          <el-table-column type="selection" width="40">
          </el-table-column>
          <!--  -->
          <el-table-column v-for="(item,i) in tableCol" :key="i" :prop="item.prop" :label="item.label" :width="item.width"></el-table-column>
          <!--  -->
          <el-table-column label="操作" v-if="showOper" fixed="right" width="130">
            <template slot-scope="scope">
              <el-button @click="handleSelect(scope.row)" type="text" size="small">查看</el-button>
              <el-button type="text" size="small" @click="handleEdit(scope.$index,scope.row)">编辑</el-button>
              <el-button size="small" type="text" @click="handleDelete(scope.$index,scope.row)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <!--  -->
        <div class="block pages_div">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.currentIndex" :page-sizes="[3, 4, 5, 6]" :page-size="4" layout="total, sizes, prev, pager, next, jumper" :total="pagination.totalItems">
          </el-pagination>
        </div>
      </div>
    </div>
    <prompt-form :visible.sync="visible" :rulePropmtForm="rowModel"></prompt-form>
  </el-container>
</template>
<script>
import { generateHtml } from '@/utils/MemberList.js'
import { generateEditForm } from '@/utils/MemberList.js'
import PromptForm from '@/components/PromptForm'
export default {
  name: "MemberList",
  components: {
    "prompt-form": PromptForm,
  },
  data() {
    return {
      rowModel: {
        role: null,
        phone: "",
        region: null,
        mailbox: "",
        department: null,
        bornTime: null,
        employeeName: "",
      },
      visible: false,
      multipleSelection: [],
      pagination: {
        currentIndex: 0, //当前页
        totalPages: 0, // 总页数
        totalItems: 0, //条目总数
        items: 4, //每页条数
      },
      showOper: true,
      tableData: [{
        bornTime: "",
        department: "",
        employeeName: "",
        employeeNum: "",
        id: "",
        mailbox: "",
        phone: "",
        regTime: "",
        region: "",
        role: "",
      }],
      tableCol: [
        { prop: 'id', label: 'id', width: 50 },
        { prop: 'bornTime', label: '出生日期', width: 160 },
        { prop: 'department', label: '部门', width: 80 },
        { prop: 'employeeName', label: '姓名', width: 100 },
        { prop: 'employeeNum', label: '工号', width: 120 },
        { prop: 'mailbox', label: '邮箱', width: 150 },
        { prop: 'phone', label: '手机号', width: 120 },
        { prop: 'regTime', label: '注册日期', width: 160 },
        { prop: 'region', label: '地区', width: 80 },
        { prop: 'role', label: '角色', width: 80 },
      ]
    }
  },
  methods: {
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pagination.items = val;
      this.getEmployeesList();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pagination.currentIndex = val;
      this.getEmployeesList();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleSelect(row) {
      console.log(row);
      var content = generateHtml(row);
      this.$alert(content, '用户资料', {
        dangerouslyUseHTMLString: true
      });
    },
    handleEdit(index, row) { //修改员工资料
      console.log(index, row);
      this.visible = true;
      this.rowModel = row;
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    getEmployeesList: function() {
      console.info(this.pagination);
      this.$axios({
          url: 'api/TAccountController/pagingSearchAction',
          method: 'get',
          params: {
            pageth: this.pagination.currentIndex,
            items: this.pagination.items
          },
          headers: {
            'token': sessionStorage.getItem('token'),
          },
        })
        .then(response => {
          console.log(response);
          if (response.data.status == 200) {
            console.log(response.data.data);
            this.tableData = response.data.data.records;
            this.pagination.totalPages = response.data.data.pages;
            this.pagination.totalItems = response.data.data.total;
            this.pagination.currentIndex = response.data.data.current;
            this.pagination.items = response.data.data.size;
          } else {
            this.$alert(response.data.message, "报错", {
              confirmButtonText: "确定"
            });
          }
        })
        .catch(function(error) {
          console.log(error)
        })
    },
  },
  mounted: function() {
    console.log(this);
    console.log('mounted:载入后html已经渲染(ajax请求可以放在这个函数中)');
    this.getEmployeesList();
  },
  updated: function() {
    console.log('update:更新状态后');
  }
}

</script>
<style>
.title_head {
  transform: translateY(-30px);
  text-align: center;
}

.pages_div {
  text-align: center;
  transform: translate(0px, 25px);
}

.employee_form span {
  font-weight: 400;
}

</style>
