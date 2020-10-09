/**
 * [judgeRegion description]
 * @param  {[type]} accRegion [description]
 * @return {[type]}           [description]
 */
function judgeRegion(accRegion) {
  let area = null;
  switch (accRegion) {
    case 0:
      area = "安淮";
      break;
    case 1:
      area = "南徽";
      break;
    case 2:
      area = "古丰";
      break;
    case 3:
      area = "方千";
      break;
    case 4:
      area = "播星";
      break;
    case 5:
      area = "角港";
      break;
    case 6:
      area = "米园";
      break;
    case 7:
      area = "湖地";
      break;
    case 8:
      area = "寿韫";
      break;
    case 9:
      area = "天玺台";
      break;
    case 10:
      area = "苏崧";
      break;
    case 11:
      area = "南威";
      break;
    case 12:
      area = "都张集";
      break;
    case 13:
      area = "企塘";
      break;
    case 14:
      area = "中缇";
      break;
  }
  return area;
}

/**
 * [judgeDept description]
 * @param  {[type]} accDepartment [description]
 * @return {[type]}               [description]
 */
function judgeDept(accDepartment) {
  var position = null;
  switch (accDepartment) {
    case 0:
      position = "研发";
      break;
    case 1:
      position = "财务";
      break;
    case 2:
      position = "人事";
      break;
    case 3:
      position = "销售";
      break;
    case 4:
      position = "公关";
      break;
    case 5:
      position = "法务";
      break;
    case 6:
      position = "安全";
      break;
  }
  return position;
}

/**
 * [formatRegTime description]
 * @param  {[type]} accRegTime [description]
 * @return {[type]}            [description]
 */
function formatRegTime(accRegTime) {
  var date = new Date(accRegTime);
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
  return gener;
}

/**
 * [judgeRole description]
 * @param  {[type]} argument [description]
 * @return {[type]}          [description]
 */
function judgeRole(argument) {
  if (argument == 0) {
    return '管理员';
  } else {
    return '普通雇员';
  }
}

/**
 * [generateHtml description]
 * @param  {[type]} argument [description]
 * @return {[type]}          [description]
 */
function generateHtml(argument) {
  console.info(argument);
  var content = "";
  content += '<form class="employee_form">';
  content += '<p> <span>ID:</span>';
  content += argument.id;
  content += '</p>';
  content += '<p> <span>工号:</span>';
  content += argument.employeeNum;
  content += '</p>';
  content += '<p><span>手机号码:</span>';
  content += argument.phone;
  content += '</p>';
  content += '<p><span>姓名:</span>';
  content += argument.employeeName;
  content += '</p>';

  content += '<p><span>出生日期:</span>';
  content += argument.bornTime;
  content += '</p>';

  content += '<p><span>邮箱:</span>';
  content += argument.mailbox;
  content += '</p>';

  content += '<p><span>账号角色:</span>';
  content += judgeRole(argument.role);
  content += '</p>';

  content += '<p><span>所属地区:</span>';
  content += judgeRegion(argument.region);
  content += '</p>';

  content += '<p><span>隶属部门:</span>';
  content += judgeDept(argument.department);
  content += '</p>';

  content += '<p><span>注册时间:</span>';
  content += formatRegTime(argument.regTime);
  content += '</p>';

  content += '</form>';
  // console.info(content);
  return content;
}

/**
 * [generateEditForm description]
 * @param  {[type]} argument [description]
 * @return {[type]}          [description]
 */
function generateEditForm(argument) {
  var content = "";
  content += "";
  content += '<div class="form_div_emp">';
  content += '<form class="submit_form_emp" id="mine_' + argument.id + '">';
  content += '<div class="submit_param_div">';
  content += '<p>请输入新手机号码:</p>';
  content += '<input type="text" value="' + argument.phone + '" max="26">';
  content += '</div>';
  content += '<div class="submit_param_div">';
  content += '<p>请选择新出生年月日:</p>';
  content += '<input type="date" value="' + formatBornTime(argument.bornTime) + '">';
  content += '</div>';
  content += '<div class="submit_param_div">';
  content += '<p>请选择新权限角色:</p>';
  content += '<p>';
  content += '管理员';
  content += '<input type="radio" value="0" name="role">';
  content += '</p>';
  content += '<p>';
  content += '普通雇员';
  content += '<input type="radio" value="1" name="role" checked>';
  content += '</p>';
  content += '</div>';
  content += '<div class="submit_param_div">';
  content += '<p>请选择新地区:</p>';
  content += '<p class="p_input push_in">';
  content += '<select value="' + argument.region + '" class="form_inp selects">';
  content += '<option value="-1"> -- 请选择所在地区 -- </option>';
  content += '<option value="0">安淮</option>';
  content += '<option value="1">南徽</option>';
  content += '<option value="2">古丰</option>';
  content += '<option value="3">方千</option>';
  content += '<option value="4">播星</option>';
  content += '<option value="5">角港</option>';
  content += '<option value="6">米园</option>';
  content += '<option value="7">湖地</option>';
  content += '<option value="8">寿韫</option>';
  content += '<option value="9">天玺台</option>';
  content += '<option value="10">苏崧</option>';
  content += '<option value="11">南威</option>';
  content += '<option value="12">都张集</option>';
  content += '<option value="13">企塘</option>';
  content += '<option value="14">中缇</option>';
  content += '</select>';
  content += '</p>';
  content += '</div>';
  content += '<div class="submit_param_div">';
  content += '<p>请选择新部门:</p>';
  content += '<p class="p_input push_in">';
  content += '<select value="' + argument.department + '" class="form_inp selects">';
  content += '<option value="-1"> -- 请选择所属部门 -- </option>';
  content += '<option value="0">研发</option>';
  content += '<option value="1">财务</option>';
  content += '<option value="2">人事</option>';
  content += '<option value="3">销售</option>';
  content += '<option value="4">公关</option>';
  content += '<option value="5">法务</option>';
  content += '<option value="6">安全</option>';
  content += '</select>';
  content += '</p>';
  content += '</div>';
  content += '<div class="submit_param_div">';
  content += '<p>请输入新邮箱:</p>';
  content += '<input type="text" value="' + argument.mailbox + '" max="38">';
  content += '</div>';
  content += '<div class="submit_param_div">';
  content += '<p>请输入新名字:</p>';
  content += '<input type="text" value="' + argument.employeeName + '" max="24">';
  content += '</div>';
  content += "<br>";
  content += '<div class="submit_param_div">';
  content += '<input type="button" value="提交" @click="submitsEdit">';
  content += '</div>';
  content += '</form>';
  content += '</div>';
  return content;
}

export {
  generateHtml,
  generateEditForm,
}
