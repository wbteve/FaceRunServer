package com.facerun.service;

import com.facerun.bean.Account;
import com.facerun.bean.AccountExample;
import com.facerun.bean.Run;
import com.facerun.bean.RunExample;
import com.facerun.dao.AccountMapper;
import com.facerun.dao.RunMapper;
import com.facerun.exception.BizException;
import com.facerun.util.Code;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xinzhendi-031 on 2017/12/1.
 */
@Service
@Transactional
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountMapper accountMapper;

    public void accountInsert(Map params){
        int type = MapUtils.getInteger(params,"type",0);
        int status = MapUtils.getInteger(params,"status",0);
        String name = MapUtils.getString(params,"name","");
        String strAccount = MapUtils.getString(params,"account","");
        String password = MapUtils.getString(params,"password","");
        Date create_at = new Date();
        if (StringUtils.isEmpty(strAccount))
            throw new BizException(Code.DATA_ERROR);
        Account account2 = accountSelect(strAccount);
        if (account2 != null && account2.getId() > 0)
            throw new BizException(Code.USER_EXIST);
        Account account = new Account();
        account.setName(name);
        account.setAccount(strAccount);
        account.setPassword(password);
        account.setType(type);
        account.setStatus(status);
        account.setCreateAt(create_at);
        int insert = accountMapper.insertSelective(account);
        if (insert != 1)
            throw new BizException(Code.FAIL_DATABASE_INSERT);
    }

    public Account accountSelect(Map params){
        int account_id = MapUtils.getInteger(params,"account_id",0);
        Account account = accountMapper.selectByPrimaryKey(account_id);
        return account;
    }

    public Account accountSelect(int account_id){
        Account account = accountMapper.selectByPrimaryKey(account_id);
        return account;
    }

    public Account accountSelect(String account){
        AccountExample example = new AccountExample();
        example.createCriteria().andAccountEqualTo(account);
        List<Account> list = accountMapper.selectByExample(example);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }
}