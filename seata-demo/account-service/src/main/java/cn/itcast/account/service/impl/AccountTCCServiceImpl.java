package cn.itcast.account.service.impl;

import cn.itcast.account.entity.AccountFreeze;
import cn.itcast.account.mapper.AccountFreezeMapper;
import cn.itcast.account.mapper.AccountMapper;
import cn.itcast.account.service.AccountService;
import cn.itcast.account.service.AccountTCCService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/22  22:18
 */
@Service
@Slf4j
public class AccountTCCServiceImpl implements AccountTCCService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountFreezeMapper accountFreezeMapper;

    @Override
    @Transactional
    public void deduct(String userId, int money) {
        String xid= RootContext.getXID();
        AccountFreeze freeze=accountFreezeMapper.selectById(xid);
        if(freeze!=null){
            return ;
        }
        accountMapper.deduct(userId,money);
        AccountFreeze accountFreeze=new AccountFreeze();
        accountFreeze.setFreezeMoney(money);
        accountFreeze.setState(AccountFreeze.State.TRY);
        accountFreeze.setUserId(userId);
        accountFreeze.setXid(xid);
        accountFreezeMapper.insert(accountFreeze);
    }

    @Override
    public boolean comfirm(BusinessActionContext ctx) {
        String xid=ctx.getXid();
        int count=accountFreezeMapper.deleteById(xid);
        return count==1;
    }

    @Override
    public boolean cancel(BusinessActionContext ctx) {
        String xid=ctx.getXid();
        AccountFreeze freeze=accountFreezeMapper.selectById(xid);
        String userId=ctx.getActionContext("userId").toString();
        if(freeze==null){
            AccountFreeze accountFreeze=new AccountFreeze();
            accountFreeze.setFreezeMoney(0);
            accountFreeze.setState(AccountFreeze.State.CANCEL);
            accountFreeze.setUserId(userId);
            accountFreeze.setXid(xid);
            accountFreezeMapper.insert(accountFreeze);
            return true;
        }
        //幂等判断
        if(freeze.getState()==AccountFreeze.State.CANCEL){
            return true;
        }
        freeze.setFreezeMoney(0);
        freeze.setState(AccountFreeze.State.CANCEL);
        int count= accountFreezeMapper.updateById(freeze)
;        return count==1;
    }
}
