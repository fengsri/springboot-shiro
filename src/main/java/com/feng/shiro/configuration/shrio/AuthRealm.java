package com.feng.shiro.configuration.shrio;

import com.feng.shiro.repository.dao.UserDao;
import com.feng.shiro.repository.domain.*;
import com.feng.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;

public class AuthRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;
    /**
     * 为用户授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权========");
        //获取前端输入的用户信息，封装为User对象
        User userweb = (User) principals.getPrimaryPrincipal();
        //获取前端输入的用户电话号码
        String tel = userweb.getTelephone();
        //根据前端输入的用户名查询数据库中对应的记录
        User user = userDao.getByTelephone(tel);
        //如果数据库中有该用户名对应的记录，就进行授权操作
        if (user != null){
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            UserRole userRole = user.getUserRole();

            //为用户授予角色
            info.addRole(userRole.getRole().getName());
            //用户授予权限
            Collection<String> permissionCollection = new HashSet<String>();
            for(RolePermission  rolePermission:userRole.getRole().getRolePermissionList()){
                permissionCollection.add(rolePermission.getPermission().getName());
            }
            info.addStringPermissions(permissionCollection);
            return info;
        }else{
            return null;
        }
    }



    /**
     * 认证登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证========");
        //token携带了用户信息
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        //获取前端输入的用户名
        String tel  = usernamePasswordToken.getUsername();
        //根据用户名查询数据库中对应的记录
        User user = userDao.getByTelephone(tel);
        if (user == null){
            throw new AuthenticationException();
        }

        //当前realm对象的name
        String realmName = getName();
       //封装用户信息，构建AuthenticationInfo对象并返回
        AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user, user.getPassword(),ByteSource.Util.bytes(user.getSalt()), realmName);
        return authcInfo;
    }


    /**
     * 重写方法,清除当前用户的的 授权缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}
