/*
 * This file is part of exam-system.
 *
 * exam-system is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * exam-system is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with exam-system. If not, see <https://www.gnu.org/licenses/>.
 */

package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.entity.Major;
import io.n0sense.examsystem.repository.MajorRepository;
import io.n0sense.examsystem.service.IMajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MajorService implements IMajorService {
    private final MajorRepository majorRepository;
    @Override
    public Major addMajor(Major major) {
        return majorRepository.save(major);
    }

    @Override
    public boolean isDuplicate(String name, Long schoolId) {
        return majorRepository.existsByNameAndSchoolId(name, schoolId);
    }

    @Override
    public Page<Major> findAll(int page, int size) {
        return majorRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Major> findAllBySchoolId(Long schoolId) {
        return majorRepository.findAllBySchoolId(schoolId);
    }

    @Override
    public Optional<Major> findById(Long id) {
        return majorRepository.findById(id);
    }
}
