package pers.fancy.tools.mongo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

import static pers.fancy.tools.mongo.TableConstant.*;


@Data
public class MBaseModel implements Serializable {

    private static final long serialVersionUID = 2109995371767737261L;

    @Id
    @Field(ID)
    @JSONField(name = "id")
    private Long _id;

    @Field(GMT_CREATE)
    @JSONField(name = GMT_CREATE)
    private String gmtCreate;

    @Field(GMT_MODIFIED)
    @JSONField(name = GMT_MODIFIED)
    private String gmtModified;

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        this._id = id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MBaseModel MBaseModel = (MBaseModel) o;
        return _id != null ? _id.equals(MBaseModel._id) : MBaseModel._id == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (_id != null ? _id.hashCode() : 0);
        return result;
    }
}
