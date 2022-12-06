select *  from emp;
select sysdate from dual;
alter session set nls_language='AMERICAN';
alter session set nls_date_format='yyyy/mm/dd';

--(1)
select empno, ENAME, SAL from emp
where DEPTNO=10;

--(2)
select ENAME,hiredate, deptno from emp
where empno=7369;

--(3)
select * from emp
where ename='ALLEN';

--(4)
select ENAME, DEPTNO, SAL from emp
where hiredate ='1991-05-01';

--(5)
select * from emp
where job!='MANAGER';

--(6)
select * from emp
where hiredate >'1991-04-02';

--(7)
select ENAME, SAL, DEPTNO from emp
where SAL >800;

--(8)
select * from emp
where DEPTNO >=20;

--(9)
select ENAME from emp
where ENAME>'K';

--(10)
select empno, ename from emp
where empno<=7698;

--(11)
select ename, sal, deptno  from emp
where hiredate between '1991/04/03' and '1992/12/09';

--(12)
select ename, job, sal  from emp
where sal>1600 and sal<3000;

--(13) 
select empno, ename  from emp
where empno between 7655 and 7781;

--(14)
select *  from emp
where Ename>='B' AND ENAME<'J';

--(15)
select HIREDATE, ename from emp
where hiredate<'1991/01/01' OR HIREDATE>'1992/01/01';

--(16)
select ename,JOB from emp
where JOB = 'MANAGER' OR JOB='SALESMAN';

--(17)
select ename,EMPNO, DEPTNO from emp
where DEPTNO != 20 AND DEPTNO != 30;
--where DEPTNO NOT IN(20, 30);

--(18)
select EMPNO, ENAME, HIREDATE, DEPTNO from emp
where Ename like 'S%';

--(19)
select ENAME, SAL, SAL*0.9 "실수령액",  DEPTNO from emp
ORDER BY 실수령액 desc;

--(20)
select ENAME, SAL, COMM, SAL+COMM "총액" from emp
where comm is not null
order by 4 desc;
