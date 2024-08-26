package com.topkey.api.dto.travel;

import java.io.Serializable;

import com.topkey.api.util.JdeDateConverterUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense implements Serializable{
	

	private static final long serialVersionUID = -4367418003433215306L;

	@Schema(description = "ERP USER ID 固定素端", example = "TKIRIS", defaultValue = "TKIRIS")
	private String vnedus = "TKIRIS"; // 素端ERP ID

	@Schema(description = "OA單號")
	private String vnedtn; // OA單號

	@Schema(description = "行號 當vnedbt異動將重新計算")
	private String vnedln; // 流水號

	@Schema(description = "傳輸日期JDE DATE")
	private String vneddt; // 傳輸日期JDE DATE

	@Schema(description = "發/收指示器 固定值", example = "B", defaultValue = "B")
	private String vneder = "B"; // Default value

	@Schema(description = "交易動作 固定值", example = "A", defaultValue = "A")
	private String vnedtc = "A"; // Default value

	@Schema(description = "交易類型 固定值", example = "J", defaultValue = "J")
	private String vnedtr = "J"; // Default value

	@Schema(description = "西元年+周數", example = "202431")
	private String vnedbt; //

	@Schema(description = "公司代碼", example = "00010", defaultValue = "00010")
	private String vnkco = "00010"; // Default value

	@Schema(description = "傳票類型 固定值", example = "JE", defaultValue = "JE")
	private String vndct = "JE"; // 傳票類型固定, default value

	@Schema(description = "同vneddt傳輸日期JDE DATE")
	private String vndgj; // 同vneddt傳輸日期JDE DATE

	@Schema(description = "公司代碼", example = "00010", defaultValue = "00010")
	private String vnco = "00010"; // Default value

	@Schema(description = "OA欄位-科目號碼")
	private String vnani; // OA欄位-科目號碼

	@Schema(description = "科目模式 固定值", example = "2", defaultValue = "2")
	private String vnam = "2"; // Default value

	@Schema(description = "OA欄位-明細帳")
	private String vnsbl; // OA欄位-明細帳

	@Schema(description = "明細類型", example = "C", defaultValue = "C")
	private String vnsblt = "C"; // Default value

	@Schema(description = "分類帳類型 固定值", example = "AA", defaultValue = "AA")
	private String vnlt = "AA"; // Default value

	@Schema(description = "世紀 固定值", example = "20", defaultValue = "20")
	private String vnctry = "20"; // Default value

	@Schema(description = "幣別 固定值", example = "NTD", defaultValue = "NTD")
	private String vncrcd = "NTD"; // Default value

	@Schema(description = "OA欄位-說明")
	private String vnexa; // OA欄位-說明

	@Schema(description = "USER ID 固定素端", example = "TKIRIS", defaultValue = "TKIRIS")
	private String vnuser = "TKIRIS"; // Default value

	@Schema(description = "工作站代號 固定值", example = "JDEAPP1", defaultValue = "JDEAPP1")
	private String vnjobn = "JDEAPP1"; // Default value

	@Schema(description = "當天")
	private String vnupmj; // 當天

	@Schema(description = "外幣/本幣", example = "D", defaultValue = "D")
	private String vncrrm = "D"; // Default value

	@Schema(description = "OA欄位-成本會計科目1")
	private String vnabr1; // OA欄位-成本會計科目1

	@Schema(description = "OA欄位-成本會計科目2")
	private String vnabr2; // OA欄位-成本會計科目2

	@Schema(description = "OA欄位-成本會計科目3")
	private String vnabr3; // OA欄位-成本會計科目3
	
	@Schema(description = "OA欄位-成本會計科目4")
	private String vnabr4; // OA欄位-成本會計科目3

	@Schema(description = "OA欄位-成本類型1")
	private String vnabt1; // OA欄位-成本類型1

	@Schema(description = "OA欄位-成本類型2")
	private String vnabt2; // OA欄位-成本類型2

	@Schema(description = "OA欄位-成本類型3")
	private String vnabt3; // OA欄位-成本類型3
	
	@Schema(description = "OA欄位-成本類型4")
	private String vnabt4; // OA欄位-成本類型3

	@Schema(description = "OA欄位-金額")
	private String vnag; // OA欄位-金額
	
	@Schema(description = "科目說明")
	private String vnexr;
	// 覆寫 setVneddt 方法
	public void setVneddt(String vneddt) {
		this.vneddt = JdeDateConverterUtil.convertToJdeDate(vneddt);
	}

	// 覆寫 setVndgj 方法
	public void setVndgj(String vndgj) {
		this.vndgj = JdeDateConverterUtil.convertToJdeDate(vndgj);
	}
	
	public void setVnupmj(String vnupmj) {
		this.vnupmj = JdeDateConverterUtil.convertToJdeDate(vnupmj);
	}

	public void setVnani(String vnani) {
		// 在设置属性值之前添加 10 个空格
		this.vnani = String.format("%10s", "") + vnani;
	}

}
