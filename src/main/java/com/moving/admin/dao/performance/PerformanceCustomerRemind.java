package com.moving.admin.dao.performance;

import com.moving.admin.dao.natives.AbstractNative;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class PerformanceCustomerRemind  extends AbstractNative {

    private final String select = "select cr.type,cr.status,cr.remark,cr.meet_time as meetTime,cr.meet_address as meetAddress,cr.meet_notice as meetNotice,cr.contact_time_start as contactTimeStart," +
            "cr.contact_time_end as contactTimeEnd,cr.create_time as createTime,u.nick_name as createUser,cr.create_user_id as createUserId,cc.name as contactName";
    private final String from = " from customer_remind cr left join customer_contact cc on cc.id=cr.contact_id left join sys_user u on u.id=cr.create_user_id left join customer c on c.id=cr.customer_id";
    private final String sort = " order by cr.create_user_id,cr.customer_id,cr.create_time desc ";
    private final String where = " where cr.create_user_id=";

    //人才常规跟踪 日、周、月绩效
    public List<Map<String, Object>> getPerformance(Long userId, Integer flag, String time) {
        String whereStr = "";
        if (StringUtils.isEmpty(time)) {
            time = "now()";
        }
        switch (flag) {
            case 1: whereStr = " and to_days(tr.create_time) = to_days('"+time+"')";break;
            case 2: whereStr = " and YEARWEEK(date_format(tr.create_time, '%Y-%m-%d')) = YEARWEEK('"+time+"', '%Y-%m-%d')";break;
            case 3: whereStr = " and DATE_FORMAT(tr.create_time, '%Y%m') = '"+time+"'";break;
        }
        return getCustomerReminds(where + userId + whereStr);
    }
    //人才常规跟踪日、 周、月报表
    public List<Map<String, Object>> getPerformanceReport(Long userId, Long roleId, Integer flag, String time) {
        String where = "";
        if (StringUtils.isEmpty(time)) {
            time = "now()";
        }
        switch (Integer.parseInt(roleId.toString())) {
            case 2:
            case 6:
            case 7: where = " where tr.create_user_id in(select user_id from team where parent_id in(select id from team where level in(2,3,4) and user_id="+userId+"))";break;
            case 3: where = " where tr.create_user_id in (select user_id from team where team_id in(select id from team where level=1 and user_id="+userId+"))";break;
            case 1: where = " where tr.create_user_id in (select user_id from team where level=1)";break;
        }
        switch (flag) {
            case 1:where = where + " and to_days(tr.create_time) = to_days('"+time+"')";break;
            case 2:where = where + " and YEARWEEK(date_format(tr.create_time, '%Y-%m-%d')) = YEARWEEK('"+time+"', '%Y-%m-%d')";break;
            case 3:where = where + " and DATE_FORMAT(tr.create_time, '%Y%m') = '"+time+"'";break;
        }
        return getCustomerReminds(where);
    }

    public List<Map<String, Object>> getCustomerReminds(String where) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Map<String, Object>> query = session.createNativeQuery(select + from + where + sort);
        query.addScalar("type", StandardBasicTypes.INTEGER);
        query.addScalar("status", StandardBasicTypes.INTEGER);
        query.addScalar("remark", StandardBasicTypes.STRING);
        query.addScalar("meetTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("meetAddress", StandardBasicTypes.STRING);
        query.addScalar("meetNotice", StandardBasicTypes.STRING);
        query.addScalar("contactTimeStart", StandardBasicTypes.TIMESTAMP);
        query.addScalar("contactTimeEnd", StandardBasicTypes.TIMESTAMP);
        query.addScalar("createTime", StandardBasicTypes.TIMESTAMP);
        query.addScalar("createUser", StandardBasicTypes.STRING);
        query.addScalar("contactName", StandardBasicTypes.STRING);
        query.addScalar("createUserId", StandardBasicTypes.LONG);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        return list;
    }


    public void appendSort(Pageable pageable) {}
}