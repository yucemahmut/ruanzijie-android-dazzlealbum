package cn.ancore.dazzlealbum.components.model;

import cn.ancore.dazzlealbum.commons.Config;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 图片数据
 * 
 * @author magicruan
 * @version 1.0.0 2012-12-21
 */

@DatabaseTable(tableName = Config.DATABASE_TABLE_PHOTO)
public class Photo extends BaseModel {

	private static final long serialVersionUID = -8151126199942645617L;

	public static final String FIELD_NAME_PHOTO_KEY = "key";

	public static final String FIELD_NAME_PHOTO_PATH = "path";

	public static final String FIELD_NAME_PHOTO_NAME = "name";

	public static final String FIELD_NAME_PHOTO_BACKGROUND = "background";

	public static final String FIELD_NAME_PHOTO_TEXT = "text";

	public static final String FIELD_NAME_PHOTO_AUDIO = "audio_id";

	public static final String FIELD_NAME_PHOTO_ALBUM = "album_id";

	@DatabaseField(columnName = FIELD_NAME_PHOTO_KEY, dataType = DataType.STRING, index = true)
	public String mPhotoKey;

	@DatabaseField(columnName = FIELD_NAME_PHOTO_ALBUM, dataType = DataType.STRING)
	public String mAlbumKey;
	
	@DatabaseField(columnName = FIELD_NAME_PHOTO_PATH, dataType = DataType.STRING, index = true)
	public String mPath;

	@DatabaseField(columnName = FIELD_NAME_PHOTO_NAME, dataType = DataType.STRING)
	public String mName;

	@DatabaseField(columnName = FIELD_NAME_PHOTO_BACKGROUND, dataType = DataType.BYTE_ARRAY)
	public byte[] mBackground;

	@DatabaseField(columnName = FIELD_NAME_PHOTO_TEXT, dataType = DataType.STRING)
	public String mText;

	@DatabaseField(columnName = FIELD_NAME_PHOTO_AUDIO, foreign = true, foreignAutoRefresh = true)
	public Audio mAudio;

	public Photo() {
		super();
	}

}
