package com.jiujun.voice.modules.apps.game.dice.domain;

import com.jiujun.voice.common.doc.annotation.DocFlag;
import com.jiujun.voice.common.jdbc.annotation.DBTable;
import com.jiujun.voice.common.model.DBModel;

/**
 * 
 * @author Coody
 *
 */
@SuppressWarnings("serial")
@DBTable("t_game_dice_record")
public class GameDiceRecord extends DBModel{

	@DocFlag("房间ID")
	private String roomId;
	@DocFlag("用户ID")
	private String userId;
	@DocFlag("摇取的值")
	private Integer diceValue;
	@DocFlag("创建时间")
	private String createTime;

	
	
	
	public Integer getDiceValue() {
		return diceValue;
	}

	public void setDiceValue(Integer diceValue) {
		this.diceValue = diceValue;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	
}
