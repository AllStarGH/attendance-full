<template>
  <!-- 常规用户 -->
  <el-container id="el_contain_0">
    <el-aside class="el_aside_1" width="200px" id="el_aside_0">
      <!-- :default-active="this.$route.path" router -->
      <el-menu :default-openeds="['0','modify']">
        <!--  -->
        <el-submenu index="0">
          <template slot="title"><i class="el-icon-message"></i>导航一</template>
          <el-menu-item-group>
            <template slot="Group 1"></template>
            <el-menu-item index="0-0" @click="jumpTo('/')">门户首页</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="常规">
            <el-menu-item index="/MyProfileShow" @click="showsAreas('MyProfileShow')">我的资料</el-menu-item>
          </el-menu-item-group>
          <!-- alter -->
          <el-submenu index="modify">
            <template slot="title">修改</template>
            <!--  -->
            <el-menu-item index="/RevampAvatar" @click="showsAreas('RevampAvatar')">更换头像</el-menu-item>
            <el-menu-item index="/EditProfile" @click="showsAreas('EditProfile')">更新资料</el-menu-item>
            <el-menu-item index="/alterKeyWord" @click="showsAreas('AlterKeyWord')">更改密码</el-menu-item>
            <!--  -->
          </el-submenu>
          <!-- /alter -->
        </el-submenu>
        <el-submenu index="2">
          <template slot="title"> <i class="el-icon-menu"></i>导航2</template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="2-1">选项2-1</el-menu-item>
            <el-menu-item index="2-2">选项2-2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="2-3">选项2-3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="2-4">
            <template slot="title">选项2-4</template>
            <el-menu-item index="2-4-1">选项2-4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title"><i class="el-icon-setting"></i>导航3</template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="3-1">选项3-1</el-menu-item>
            <el-menu-item index="3-2">选项3-2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组二">
            <el-menu-item index="3-3">选项3-3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="4">
            <template slot="title">选项4</template>
            <el-menu-item index="3-4-1">选项3-4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
      </el-menu>
    </el-aside>
    <!-- 以上为侧边栏 -->
    <el-container>
      <el-header id="header_0" class="el_head_00">
        <Avatar />
        <el-dropdown>
          <i class="el-icon-setting" id="i_setting"></i>
          <el-dropdown-menu>
            <el-dropdown-item>更换头像</el-dropdown-item>
            <el-dropdown-item>查看资料</el-dropdown-item>
            <el-dropdown-item>更改密码</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <div class="emp_name">
          <b>{{accEmployeeName}}</b>
        </div>
      </el-header>
      <!-- 以上为顶栏 -->
      <el-main>
        <AlterKeyWord :style="alterKeyWord"></AlterKeyWord>
        <EditProfile :style="editProfile"></EditProfile>
        <RevampAvatar :style="revampAvatar"></RevampAvatar>
        <MyProfileShow class="mine_tag_1" :style="myProfileShow"></MyProfileShow>
        <!--  -->
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
import Avatar from '@/components/Avatar'
import MyProfileShow from '@/components/MyProfileShow'
import EditProfile from '@/views/alter/EditProfile'
import RevampAvatar from '@/views/alter/RevampAvatar'
import AlterKeyWord from '@/views/alter/AlterKeyWord'
export default {
  name: 'NormalUser',
  components: {
    Avatar,
    MyProfileShow,
    EditProfile,
    RevampAvatar,
    AlterKeyWord,
  },
  data() {
    return {
      revampAvatar: {
        display: 'none',
      },
      editProfile: {
        display: 'none',
      },
      myProfileShow: {
        display: 'block',
      },
      alterKeyWord: {
        display: 'none',
      },
      accEmployeeName: sessionStorage.getItem('accEmployeeName'),
    }
  },
  methods: { //computed是属性调用，而methods是函数调用
    jumpTo(uri) {
      this.$router.push(uri)
    },
    showsAreas(param) {
      if (param == 'RevampAvatar') {
        this.editProfile.display = 'none';
        this.myProfileShow.display = 'none';
        this.alterKeyWord.display = 'none';

        this.revampAvatar.display = 'block';

      } else if (param == 'EditProfile') {
        this.revampAvatar.display = 'none';
        this.myProfileShow.display = 'none';
        this.alterKeyWord.display = 'none';

        this.editProfile.display = 'block';

      } else if (param == 'MyProfileShow') {
        this.editProfile.display = 'none';
        this.revampAvatar.display = 'none';
        this.alterKeyWord.display = 'none';

        this.myProfileShow.display = 'block';

      } else if (param == 'AlterKeyWord') {
        this.editProfile.display = 'none';
        this.revampAvatar.display = 'none';
        this.myProfileShow.display = 'none';

        this.alterKeyWord.display = 'block';

      }

    },
  },
  computed: { //computed 不能计算data() 中的属性
    onRoutes() {
      return this.$route.path.replace('/', '');
    },
  },
  mounted() {
    console.log(this);
  }
}

</script>
<style src="../../styles/NormalUser.css"></style>
<style scoped>
b {
  color: #656465;
}

.el_head_00 {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el_aside_1 {
  color: #333;
}

.el-dropdown {
  transform: translate(-60px, -50px);
}

.emp_name {
  transform: translate(880%, -109px);
  width: 180px;
}

</style>
