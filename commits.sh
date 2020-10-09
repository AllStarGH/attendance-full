#git init

#origin:远端仓库的名字
#git remote add origin https://github.com/AllStarGH/attendance-full.git

git status

# 表示添加所有内容
git add -A

# 表示添加所有 改变过 的内容
#git add .

git commit -m "revamps the README"

#master:当前分支名
git pull origin master

#报错:fatal: 无法找到远程引用 master
#首次提交,可忽略不计

git push origin master  
