package com.example.demo.exception;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.example.demo.dto.ApiResult;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异常拦截
 * note: 所有内部业务异常均捕获，以 {"message":"服务器内部错误","status":500} 返回异常信息
 */
@Activate(group = Constants.PROVIDER)
public class ExceptionFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ExceptionFilter.class);

    /** 参数校验器 fail fast*/
    private static ValidatorFactory vf = Validation.byProvider(HibernateValidator.class).configure().addProperty("hibernate.validator.fail_fast", "true").buildValidatorFactory();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        try {
//            暂停服务，停止投入
//            Result validateReulst = ValidationParams(invocation);
//            result = validateReulst == null ? invoker.invoke(invocation) : validateReulst;
            result = new RpcResult(new CommonException(ExceptionCode.SERVICE_UNAVAILABLE, "暂停服务"));
            if (result.hasException()) {
                Throwable exception = result.getException();
                ApiResult apiResult = new ApiResult();
                // 自定义异常
                if (exception instanceof CommonException) {
                    CommonException commonException = (CommonException) exception;
                    apiResult.setStatus(commonException.getExceptionCode().getCode());
                    apiResult.setMessage(StringUtils.join(new String[]{commonException.getExceptionCode().getMsgHead(), exception.getMessage()}, ':'));
                } else {
                    // 非自定义的异常统一做: 服务器内部错误处理
                    apiResult.setStatus(ExceptionCode.INTERNAL_SERVER_ERROR.getCode());
                    apiResult.setMessage(ExceptionCode.INTERNAL_SERVER_ERROR.getMsgHead());
                    log.error("Exception of service(" + invoker + " -> " + invocation + ")", exception);
                }
                return new RpcResult(apiResult);
            }
        } catch (RpcException e) {
            log.error("RpcException:", e);
        }
        return result;
    }

    /**
     * 参数合法性校验
     * @param invocation
     * @return
     */
    public Result ValidationParams(Invocation invocation) {
        Class<?>[] parameterTypes = invocation.getParameterTypes();
        Validator validator = vf.getValidator();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (validator.getConstraintsForClass(parameterTypes[i]).isBeanConstrained()) {
                Set<ConstraintViolation<Object>> validate = validator.validate(invocation.getArguments()[i]);
                if (!validate.isEmpty()) {
                    return new RpcResult(new CommonException(ExceptionCode.REQEUST_EXC, validate.iterator().next().getMessageTemplate()));
                }
            }
        }
        return null;
    }

//    public static void main(String[] args) {
//        //service: 46 method: 25 response:80 cost: 9
//        System.out.println("+-------------------------------------------------------------------------------------------+");
//        System.out.println("|service  | cn.demo.service.xxxxxxxxxxxxxxxxxxxx |method| xxxxxxxxxxxxxxxxxxxxx    |");
//        System.out.println("|-------------------------------------------------------------------------------------------|");
//        System.out.println("|request  | [{\"borrowUserId\":1000136496,\"cgbCustAccno\":\"0003PERBOR9000136496\",\"contractNo\":\"L00010220160728113\",\"transSerialNo\":\"924548\"},\"REFUND\",\"REFUND_ACCOUNT\",\"\",26,145433,\"\"]|");
//        System.out.println("|-------------------------------------------------------------------------------------------|");
//        System.out.println("|response |                                                                                 |");
//        System.out.println("|-------------------------------------------------------------------------------------------|");
//        System.out.println("|request-time | 2018-12-28 17:13:11 | response-time | 2018-12-28 17:13:11 | cost | 1000s    |");
//        System.out.println("+-------------------------------------------------------------------------------------------+");
//    }
}
