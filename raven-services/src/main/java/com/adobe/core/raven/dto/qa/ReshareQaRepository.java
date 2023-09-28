package com.adobe.core.raven.dto.qa;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;

public @Data class ReshareQaRepository {


	private String id; // ref from message id
	private String status;
	private String bu;
	private String segment;
	private HashSet<CheckList> checkList;
}
