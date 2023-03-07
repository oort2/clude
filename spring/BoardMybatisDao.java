package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import model.Board;
import model.Member;

@Repository 
public class BoardMybatisDao{
	
@Autowired
SqlSessionTemplate session;	
	
	private static final String ns = "board.";
	private Map map = new HashMap();
	
	public int insertBoard(Board board) {  
		
		int num= session.insert(ns + "insertBoard", board);   
		
		return num;
	}
	
	public int boardCount(String boardid) { 
		
		int boardCount=session.selectOne(ns+ "boardCount", boardid);
		
		return boardCount;
	}
	
	public List<Board> boardList(String boardid, int pageInt, int limit) { 
		/* 1 page  1~3
		 * 2 page  4~6
		 * 3 page  7~9
		 *  (pageInt-1)*limit+1 ~ pageInt*limit
		 */
		
		map.clear();
		map.put("boardid", boardid);
		map.put("start", (pageInt-1)*limit+1);
		map.put("end", pageInt*limit);
		
		
		List<Board> list=session.selectList(ns + "boardList", map);
		
		return list;
	}
	
	public Board boardOne(int num) { 
		
		Board board =session.selectOne(ns + "boardOne", num);
	 
		return board;
	}
	
	public void refStepAdd(int ref, int refstep) { 
		
		map.clear();
		map.put("ref", ref);
		map.put("refstep", refstep);
		session.update(ns + "refStepAdd", map);
	
	}
	
	public int insertReply(Board board) { 
	
		int num =session.insert(ns + "insertReply", board);
		
		return num;
	}
	
	public int boardDelete(int num, String pass) { 
		
		map.clear();
		map.put("num", num);
		map.put("pass", pass);
		
		int count =session.insert(ns + "boardDelete", map);
		
		return count;
	}
	
	public int boardUpdate(Board board) { 
		int num = session.update(ns+"boardUpdate", board);
		return num;
	}
	
	
}
