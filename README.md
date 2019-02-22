
# 子模块
使用git维护公共模块

#### 更新子模块
```
git submodule update
```
#### 删除子模块方法

```
git rm --cached moduleA
rm -rf moduleA
rm .gitmodules
vim .git/config
```