package cn.ancore.dazzlealbum.components.model;

import cn.ancore.dazzlealbum.commons.Config;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName=Config.DATABASE_TABLE_ALBUM)
public class Album extends BaseModel{

	private static final long serialVersionUID = -7617006912042852392L;

	public static final String FIELD_NAME_ALBUM_NAME = "name";
	
	public static final String FIELD_NAME_ALBUM_THUMB = "thumb";
	
	
	//ForeignCollectionField
	
}
