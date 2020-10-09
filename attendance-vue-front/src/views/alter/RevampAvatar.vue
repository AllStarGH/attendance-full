<template>
  <div class="div_pro_0" ref="div_pro_1">
    <form class="own_form_pro_1">
      <div class="in_p_img">
        <!--  -->
        <div>
          <img :src="avatar" ref="preview_portrait" class="img_avatar">
        </div>
        <!--  -->
        <div class="input_and_submit">
          <div class="input_file_div">
            <input type="file" accept="image/gif,image/jpeg,image/jpg,image/png" @change="changeImage($event)" ref="avatarInput" class="upload_picture" name="portrait" value="" placeholder="" id="up_pic">
          </div>
          <!--  -->
          <div class="submit_btn" @click="submitButton">
            <div class="submit_ok">
              确定
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>
<script>
export default {
  inject: ['reload'], //注入依赖
  name: 'RevampAvatar',
  data() {
    return {
      avatar: require('../../assets/vueRoute.jpg'),
      fileImg: Object,
      imgData: {
        acceptList: 'image/gif,image/jpeg,image/jpg,image/png',
      }
    }
  },
  methods: {
    submitButton() {
      var formData = new FormData();
      // var file=this.$refs.avatarInput.files[0];
      if (this.avatar == '/static/img/vueRoute.0ca67ae.jpg') {
        this.$notify({
          title: 'Error',
          message: '您尚未选择图片',
          type: 'warning'
        });
        return;
      }

      formData.append('portrait', this.avatar);
      formData.append('token', sessionStorage.getItem("token"));

      this.$axios.post(
          '/api/TAccountController/uploadAvatarAction1',
          formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            },
          }
        )
        .then(response => {
          console.log(response);
          if (response.data.status === 200) {
            console.log(response.data.data);
            if (response.data.data != null) {
              this.$notify({
                title: '成功',
                message: '更换头像成功',
                type: 'success'
              });
              sessionStorage.setItem("accPortrait", response.data.data.uploadAvatar);
              this.avatar = response.data.data.uploadAvatar;
              mine.reload();
            }
          } else {
            this.$alert(response.data.message, "提示", {
              confirmButtonText: "确定"
            });
          }
        }).catch(function(err) {
          console.log(err);
        })

    },
    changeImage(e) {
      console.log(e);
      var that = this;
      var reader = new FileReader();

      var file = e.target.files[0];
      this.fileImg = file;
      console.log(file);

      if (file.size > 0.1 * 1024 * 1024) { /*限制上传图片大小<=1MB*/
        this.$notify({
          title: 'Error',
          message: '图片大小必须小于等于100kb,请重新选择图片',
          type: 'warning'
        });
        return;
      }

      /*划定图片文件格式*/
      if (this.imgData.acceptList.indexOf(file.type) == -1) {
        this.$notify({
          title: 'Error',
          message: '图片格式必须是JPEG/JPG/PNG/GIF,请重新选择图片',
          type: 'warning'
        });
        return;
      }
      console.info('file-type accessible success');

      reader.readAsDataURL(file); //将文件读取为DataURL
      console.log(file);

      reader.onload = function(e) {
        //选择图片时即时预览
        that.avatar = this.result;
      }
    },

  },
  mounted: function() {
    console.log(this);
  }
}

</script>
<style scoped>
.div_pro_0 {
  /*display: none;*/
  transform: translateY(16px);
}

.upload_picture {
  margin: 20px auto auto auto;
  z-index: 99999;
  transform: translateX(1%);
  font-size: 20px;
}

.submit_ok {
  line-height: 35px;
}

.submit_btn {
  width: 30%;
  height: 35px;
  background: #FF6600;
  border-radius: 6px;
  margin: 20px auto auto auto;
  text-align: center;
  line-height: 0.8rem;
  color: #FFFFFF;
  cursor: pointer;
  transform: translate(-10%, 38px);
}

.img_avatar {
  position: absolute;
  height: 190px;
  width: 200px;
  transform: translate(345%, 0px);
}

.input_and_submit {
  transform: translateY(195%);
}

</style>
