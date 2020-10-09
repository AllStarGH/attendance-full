import Vue from 'vue'
import Router from 'vue-router'

import Facade from '@/views/Facade'
import Admin from '@/views/account/Admin'
import Login from '@/views/Login'
import Profile from '@/views/profile'
import Introd from '@/views/introd'
import Register from '@/views/account/Register'
import NormalUser from '@/views/account/NormalUser'
import EditProfile from '@/views/alter/EditProfile'
import RevampAvatar from '@/views/alter/RevampAvatar'
import MyProfileShow from '@/components/MyProfileShow'
import AlterKeyWord from '@/views/alter/AlterKeyWord'
import MemberList from '@/views/retrieve/MemberList'
import Regist from '@/views/account/Regist'

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: '/',
  routes: [{
      path: '/',
      name: 'Facade',
      component: Facade,
      //alias“别名”:/a 的别名是 /b，意味着当用户访问 /b 时，URL 会保持为 /b，但是路由匹配则为 /a，就像用户访问 /a 一样
      alias: '/attendance'
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
      alias: '/attendance/register'
    },
    { // new regist vue
      path: '/regist',
      name: 'Regist',
      component: Regist,
      alias: '/attendance/regist'
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      alias: '/attendance/login'
    },
    {
      path: '/NormalUser',
      name: 'NormalUser',
      component: NormalUser,
      alias: '/attendance/NormalUser',
    },
    {
      path: '/admin',
      name: 'Admin',
      component: Admin,
      redirect: '/admin/profile',
      children: [{
          path: 'profile',
          name: '资料',
          component: Profile,
          children: [{
            path: 'profile',
            name: '资料页',
            component: Profile
          }, {
            path: 'profile1',
            name: '资料页1',
            component: Profile,
            children: [{
              path: 'profile1-0',
              name: '资料页1-0',
              component: Profile
            }, {
              path: 'profile1-1',
              name: '衍生页1-1',
              component: Profile
            }]
          }, ]
        },
        {
          path: 'Introd',
          name: '简介',
          component: Introd,
          alias: '/attendance/Introd'
        },
        {
          path: 'MemberList',
          name: '列表',
          component: MemberList,
          alias: '/attendance/MemberList',
        },
      ]
    }
  ]
})
