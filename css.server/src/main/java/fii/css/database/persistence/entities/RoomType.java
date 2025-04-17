package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("RoomType")
public class RoomType extends DatabaseEntity {
    @Id
    @Column("room_type_id")
    private int roomTypeId;

    @Column("type_name")
    private String typeName;

    public RoomType() {}

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
