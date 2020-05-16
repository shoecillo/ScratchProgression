package com.sh.scratch.app.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sh.scratch.app.dto.ScratchFailsDto;

@RestController
public class ScratchController {

	@Value("${stats.file}")
	private String statsFile;
	
	private static Logger LOGGER = LoggerFactory.getLogger(ScratchController.class);
	
	@GetMapping("/startSession")
	public ResponseEntity<List<ScratchFailsDto>> startSession() throws Exception{
		
		List<ScratchFailsDto> ls = readStats();
		ScratchFailsDto dto;
		if(ls.isEmpty()) {
			dto = new ScratchFailsDto();
		} else {
			dto = ls.get(ls.size()-1);
			if(dto.getEndTime() == null) {
				return new ResponseEntity<>(HttpStatus.ACCEPTED);
			}
		}
		dto = new ScratchFailsDto();
		Instant utc = Instant.now();
		ZoneId zId = ZoneId.of("Europe/Paris");
		ZonedDateTime dt = ZonedDateTime.ofInstant(utc, zId);
		dto.setStartTime(Date.from(dt.toInstant()));
		ls.add(dto);
		writeStats(ls);
		LOGGER.info("START SESSION");	
		return new ResponseEntity<>(ls,HttpStatus.CREATED);
	}
	
	@GetMapping("/stopSession")
	public ResponseEntity<List<ScratchFailsDto>> stopSession() throws Exception{
		
		List<ScratchFailsDto> ls = readStats();		
		ScratchFailsDto dto;
		if(ls.isEmpty()) {
			dto = new ScratchFailsDto();
		} else {
			dto = ls.get(ls.size()-1);
		}
		if(dto.getEndTime() == null) {
			dto.setEndTime(new Date());
			ls.set(ls.size()-1, dto);
			writeStats(ls);
			LOGGER.info("STOP SESSION");
			
			return new ResponseEntity<>(ls,HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	}
	
	
	@GetMapping("/addFail")
	public ResponseEntity<List<ScratchFailsDto>> addFail() throws Exception{
		
		List<ScratchFailsDto> ls = readStats();
		
		ScratchFailsDto dto;
		if(ls.isEmpty()) {
			dto = new ScratchFailsDto();
		} else {
			dto = ls.get(ls.size()-1);
		}
		
		List<Date> lsDt = dto.getFails();
		lsDt.add(new Date());
		dto.setFails(lsDt);
		ls.set(ls.size()-1, dto);
		writeStats(ls);
		LOGGER.info("ADD FAIL TO SESSION");
		
		return new ResponseEntity<>(ls,HttpStatus.CREATED);
	}
	
	@GetMapping("/getStats")
	public ResponseEntity<List<ScratchFailsDto>> getStats() throws Exception{
		
		List<ScratchFailsDto> ls = readStats();
		
		return new ResponseEntity<>(ls,HttpStatus.CREATED);
	}
	
	@GetMapping("/backupStats")
	public ResponseEntity<List<ScratchFailsDto>> backupStats() throws Exception{
		backupFile();
		List<ScratchFailsDto> ls = readStats();
		return new ResponseEntity<List<ScratchFailsDto>>(ls, HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	private List<ScratchFailsDto> readStats() throws Exception {
		Path p = Paths.get(statsFile);
		if(p.toFile().exists()) {
			FileInputStream fIstr  = new FileInputStream(p.toFile());
			ObjectInputStream iStr = new ObjectInputStream(fIstr);
			List<ScratchFailsDto> lsRes = (List<ScratchFailsDto>) iStr.readObject();
			iStr.close();
			fIstr.close();
			return lsRes;
		} else {
			return new ArrayList<ScratchFailsDto>();
		}
		
	}
	
	private void writeStats(List<ScratchFailsDto> dto) throws Exception {
		Path p = Paths.get(statsFile);
		FileOutputStream fOstr = new FileOutputStream(p.toFile());
		ObjectOutputStream iOstr = new ObjectOutputStream(fOstr);
		iOstr.writeObject(dto);
		iOstr.close();
		fOstr.close();
	}
	
	private void backupFile() throws Exception {
		Path p = Paths.get(statsFile);
		Path parent = p.toAbsolutePath().getParent();
		String filename  = "backup-".concat(String.valueOf(System.currentTimeMillis())).concat("-").concat(p.getFileName().toString());
		Path res = parent.resolve(filename);
		
		
		FileOutputStream oStr = new FileOutputStream(res.toString());
		Files.copy(p, oStr);
		oStr.close();
		Files.delete(p);
		
		LOGGER.info(res.toString());
	}
	
}
