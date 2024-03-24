package cn.itcast.account.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/22  22:10
 */
@LocalTCC
public interface AccountTCCService {
    @TwoPhaseBusinessAction(name="deduct",commitMethod = "comfirm",rollbackMethod = "cancel")
    void deduct(@BusinessActionContextParameter(paramName = "userId")String userId,
                @BusinessActionContextParameter(paramName = "money")int money);

    boolean comfirm(BusinessActionContext ctx);
    boolean cancel(BusinessActionContext ctx);
}
