package com.moving.admin.entity.customer;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel("客户")
@Data
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "状态：0 普通客户， 1 扩展客户， 2 客户")
    private Integer type;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "网址")
    private String website;

    @ApiModelProperty(value = "规模")
    private String scale;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "福利")
    private String welfare;

    @ApiModelProperty(value = "简历")
    private String description;

    @ApiModelProperty(value = "合同")
    private String contractUrl;

    @ApiModelProperty(value = "关注状态")
    private Boolean follow;

    @ApiModelProperty(value = "创建者ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime = new Date(System.currentTimeMillis());

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @Transient
    private Long projectCount;

    @Transient
    private Date lastFollowTime;
}
