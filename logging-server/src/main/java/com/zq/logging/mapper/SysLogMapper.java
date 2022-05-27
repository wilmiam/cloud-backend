/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.zq.logging.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zq.logging.entity.SysLog;
import org.springframework.stereotype.Repository;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Repository
public interface SysLogMapper extends BaseMapper<SysLog> {

}
