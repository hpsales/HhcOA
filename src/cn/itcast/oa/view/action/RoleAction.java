package cn.itcast.oa.view.action;

import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.oa.base.ModelDrivenBaseAction;
import cn.itcast.oa.domain.Privilege;
import cn.itcast.oa.domain.Role;

import com.opensymphony.xwork2.ActionContext;

@Controller	//在struts里面class的值可以直接使用roleAction
@Scope("prototype")	//多例--解决线程安全
public class RoleAction extends ModelDrivenBaseAction<Role> {

	//写上privilegeIds的getter()和setter()则会自动将请求参数封装到privilegeIds中
	private Long[] privilegeIds;

	/** 列表 */
	public String list() throws Exception {
		//数据放入action上下文中，actionContext本身 就是一个Map
		List<Role> roleList = roleService.findAll();
		ActionContext.getContext().put("roleList", roleList);
		return "list";
	}

	/** 删除 */
	public String delete() throws Exception {
		roleService.delete(model.getId());
		return "toList";
	}

	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}

	/** 添加 */
	public String add() throws Exception {
		// 封装对象
		// Role role = new Role();
		// role.setName(model.getName());
		// role.setDescription(model.getDescription());

		// 保存到数据库(model中正好只有我们需要存进数据库的两个属性:name、describle，没有多余和少的)
		roleService.save(model);

		return "toList";
	}

	/** 修改页面 */
	public String editUI() throws Exception {
		// 准备要回显的数据
		Role role = roleService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(role);	//将role放入值栈中
		return "saveUI";
	}

	/** 修改 */
	public String edit() throws Exception {
		// 1，从数据库中获取要修改的原始对象
		Role role = roleService.getById(model.getId());

		// 2, 设置要修改的属性
		role.setName(model.getName());
		role.setDescription(model.getDescription());

		// 3，更新到数据库
		roleService.update(role);

		return "toList";
	}

	/** 设置权限页面 */
	public String setPrivilegeUI() throws Exception {
		// 准备数据
		List<Privilege> topPrivilegeList = privilegeService.findTopList();
		ActionContext.getContext().put("topPrivilegeList", topPrivilegeList);

		// 准备要回显的数据
		Role role = roleService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(role);

		// 准备回显的权限数据
		privilegeIds = new Long[role.getPrivileges().size()];
		int index = 0;
		for (Privilege p : role.getPrivileges()) {
			privilegeIds[index++] = p.getId();
		}

		return "setPrivilegeUI";
	}

	/** 设置权限 */
	public String setPrivilege() throws Exception {
		// 1，从数据库中获取要修改的原始对象
		Role role = roleService.getById(model.getId());

		// 2, 设置要修改的属性
		List<Privilege> privilegeList = privilegeService.getByIds(privilegeIds);
		role.setPrivileges(new HashSet<Privilege>(privilegeList));

		// 3，更新到数据库
		roleService.update(role);

		return "toList";
	}

	// ---

	public Long[] getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(Long[] privilegeIds) {
		this.privilegeIds = privilegeIds;
	}

}
